require 'server.rb'
require 'rmail'

message = RMail::Parser.read($stdin.read)

maildomain = ARGV[0]
mqserver = ARGV[1]
mquser = ARGV[2]
mqpasswd = ARGV[3]

recipients = message.header.recipients.addresses
recipients.push(message.header["X-Original-To"])

server = MindquarryTalk::Server.new(mqserver, mquser, mqpasswd)

# we need at least one recipient
if not recipients.length or not recipients.length
  exit 67
end

recipient = recipients[0]
if not recipient["@#{maildomain}"]
  # the recipient is not in our domain
  exit 67
end

puts "recipient: #{recipient}"

addressContainsConversation = recipient[/\..*@/]

# extract the team name
if addressContainsConversation
  teamID = recipient[/\..*@/][1..-2]
else
  teamID = recipient[/.*@/][0..-2]
end 

# get the team
team = server.getTeam(teamID)

# team not found?
if not team.name
  puts "no such team: #{teamname}"
  exit 67
end

if addressContainsConversation
  # get existing conversation
  conversationID = recipient[/.*?\./][0..-2]
  puts "conversation ID: #{conversationID}"
  conversation = team.getConversation(conversationID)
else
  # create new conversation
  subject = message.header.subject
  exit 77 unless subject
  conversation = team.newConversation(subject)
end

# conversation not found / could not be created?
if not conversation.title
  puts "Error: failed to get or create conversation"
  exit 67
end

# post the message 
from = message.header.from.first.address
postmsg = conversation.postMessage(from, message.body)

# message post failed?
if not postmsg
  puts "Error: permission denied"
  exit 69
end

puts "posted message from #{from}"

