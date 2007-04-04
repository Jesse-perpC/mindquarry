require 'server.rb'
require 'rmail'

message = RMail::Parser.read($stdin.read)
maildomain = "localhost"
mqserver = "http://localhost:8888"
mquser = "admin"
mqpasswd = "admin"

puts "From " + message.header.from.first.address
recipients = message.header.recipients.addresses
recipients.push(message.header["X-Original-To"])

server = MindquarryTalk::Server.new(mqserver, mquser, mqpasswd)

puts server

for recipient in recipients
  # if the domain is our maildomain
  if recipient["@#{maildomain}"]
    #if there is a dot in (e.g. Foobar.demo@...)
    if recipient[/\..*@/]
      #the team is everything behind the dot
      team = recipient[/\..*@/][1..-2]
      conversation = recipient[/.*\./][0..-2]
      puts "Team: " + team
      puts "Conversation: " + conversation
      
      
    else
      team = recipient[/.*@/][0..-2]
      puts "Team:" + team
    end
  end
end
