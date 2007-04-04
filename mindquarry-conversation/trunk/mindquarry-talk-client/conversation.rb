require 'net/http'
require 'rexml/document'

require 'message.rb'

module MindquarryTalk

  class Conversation
  
    attr_reader :team, :id, :title
  
    def initialize(team, id, title = nil)
      @team = team
      @id = id
      @server = team.server
      @title = title
    end
    
    def getMeta
      @server.http.start do |http|
        request = @server.getRequestForPath "/talk/#{@team.id}/#{@id}/meta"
        response = http.request(request)
        doc = REXML::Document.new(response.body)
        doc.root.each_element do |node|
          if node.name == "title"
            @title = node.text
          end
        end
      end
    end
    
    def getMessages
      messages = []
      @server.http.start do |http|
        request = @server.getRequestForPath "/talk/#{@team.id}/#{@id}/"
        response = http.request(request)
        doc = REXML::Document.new(response.body)
        doc.root.each_element do |node|
          messages.push Message.new(self, node.attribute('id').value)
        end
      end
      messages.each do |msg|
        msg.readMessage
      end
      return messages
    end
    
    def postMessage(from, body = "hello")
      @server.http.start do |http|
        request = @server.putRequestForPath "/talk/#{@team.id}/#{@id}/new"
        request["content-type"] = "application/xml"
        message = REXML::Element.new "message"
        message.add_attribute("via","mail")
        message.add_element("from").add_text(from)
        message.add_element("body").add_text(body)
        body = "";
        message.write(body)
        request.body = body;
        puts request.path
        puts request.body
        
        response = http.request(request)
        
        
        puts "response #{response.class}:"
        puts response.body
        
        url = response["Location"]
        Message.new(self, url[url.rindex('/')+1..-1])
      end
    end
  end
  
end