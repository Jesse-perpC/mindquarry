
require 'server.rb'

server = MindquarryTalk::Server.new("http://localhost:8888", "admin", "admin")

teams = server.getTeams

# teams[0].newConversation("Foo Bar")
# teams[0].newConversation("Foo Bar A")
# teams[0].newConversation("Foo Bar B")
# teams[0].newConversation("Foo Bar C")

# conv = teams[0].newConversation("Foo Bar E")

# conv = teams[0].getConversations[-1]

#puts conv.postMessage("lars", "bin grad nicht da").id
# 
# sleep 0.13
# 
#puts conv.postMessage("jonas", "naja, nicht so schlimm").id
# 
# sleep 1
# 
# puts conv.postMessage("lars", "ja, ok").id
# 
# sleep 1
# 
# puts conv.postMessage("alex", "ooooch").id

teams.each do |team|
  puts "Team \"#{team.name}\""
  team.getConversations.each do |conv|
    puts "  Conversation \"#{conv.title}\""
    conv.getMessages.each do |msg|
      puts "    #{msg.id} - #{msg.from}: #{msg.body}"
    end
  end
end



