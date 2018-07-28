# DOCUMENTATION FOR MAIL ADAPTER

## General info
This module is thread safe

## Senders
### FakeMailSender
Mail sender for environment without running SMTP server. Simulates sending sms via logging it

### MailSender
Not encrypted mail sender. It is not secure.

### Future work:
Add secure implementation on SSL/TLS. It require additional configuration on testbed.


## Mail
### Mail required fields are:
- receiver
- sender
- title
- html or plain text message

### Mail may contains:
- more than one receivers
- CC receivers
- BCC receivers
- attachments

