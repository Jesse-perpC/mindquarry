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

for recipient in recipients
  # if the domain is our maildomain
  if recipient["@#{maildomain}"]
    #if there is a dot in (e.g. Foobar.demo@...)
    if recipient[/\..*@/]
      #the team is everything behind the dot
      team = server.getTeam(recipient[/\..*@/][1..-2])
      # no user
      return 67 unless team.name
      conversation = team.getConversation(recipient[/.*\./][0..-2])
      return 67 unless conversation.title
      postmsg = conversation.postMessage(message.header.from.first.address, message.body)
      return 69 unless postmsg
    else
      team = server.getTeam(recipient[/.*@/][0..-2])
      return 67 unless team.name
      subject = message.header.subject
      #permission denied
      return 77 unless subject
      conversation = team.newConversation(message.header.subject)
      return 67 unless conversation
      postmsg = conversation.postMessage(message.header.from.first.address, message.body)
      return 69 unless postmsg
    end
  end
end
