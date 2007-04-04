
require 'net/http'
require 'rexml/document'

module MindquarryTalk
  
  class Message
    attr_reader :id, :body, :from
  
    def initialize(conversation, id)
      @conversation = conversation
      @id = id
      @team = conversation.team
      @server = @team.server
    end
    
    def readMessage
      @server.http.start do |http|
        request = @server.getRequestForPath "/talk/#{@team.id}/#{@conversation.id}/#{@id}"
        response = http.request(request)
        doc = REXML::Document.new(response.body)
        doc.root.each_element do |node|
          if node.name == "body"
            @body = node.text
          else node.name == "from"
            @from = node.text
          end
        end
      end
    end
  end
  
end