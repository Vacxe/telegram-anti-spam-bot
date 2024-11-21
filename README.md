Telegram Anti Spam Bot

Before start:
* Message @BotFather
* Create new bot
* Set permission to read chat messages
* Create "Quarantine" chat and add bot into it
* Add bot into your "Main" chat with "Delete message" and "Ban" permissions

Start bot:
* Create `data` directory
* Add `config.yaml` with

  ```yaml
  token: "<BOT-TOKEN>"
  pollingTimeout: 10
  ```

* Open "Quarantine" chat and call `/get_chat_id` command. It will return chat ID. Copy it.
* Open "Main" chat and call `/set_admin_chat_id <ID FROM PREVIOUS STEP>`
* Call `/enable`
