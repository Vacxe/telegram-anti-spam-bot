## 🛡️ SpamFighters – Open-Source Telegram Antispam Bot
A powerful, modular antispam bot for Telegram with customizable filtering strategies per chat.
Built for communities, powered by AI and flexible rules.

---

## ⚙️ Setup Steps

Follow these steps to get your Telegram antispam bot up and running:

### 1. Create a Log Group

* Create a **Telegram group** that will serve as the **log/admin chat**.
* Add `@spam_eater_bot` to the group.
* Grant the bot **admin permissions** (at minimum: read messages and send messages).

This group will receive logs and alerts about spam detection and actions taken.

### 2. Get the Chat ID

Send the `/get_chat_id` command in the log group.

* The bot will reply with the group’s ID.
* **Copy this value** — you'll need it in the next steps.

### 3. Add the Bot to Your Main Chat

* Invite `@spam_eater_bot` to the chat you want to protect.
* Give it **admin permissions**:

  * Can delete messages
  * Can restrict members
  * Can read all messages

### 4. Enable the Bot

In your main chat, send the following command:

```text
/enable
```

This activates spam filtering for the group.

### 5. Set the Log Chat ID

Back in your main chat, link the log group by sending:

```text
/set_admin_chat_id <your_log_chat_id>
```

Example:

```text
/set_admin_chat_id -1001234567890
```

You're all set! 🛡️ The bot is now actively protecting your group and logging its actions.

---

## 🧪 Setup Filters
After adding and enabling the bot in your chat, you can configure its behavior using custom filters.

## 📥 Upload Filter Configuration
To use your own filter rules:

Open the log/admin chat (the one you linked with /set_admin_chat_id).

Send the file filters.yaml as a document (not as text or photo).

The bot will automatically load and apply the filters from the file.

* ✅ You’ll see a confirmation message if the filters were loaded successfully.
* ❌ If there’s an error in the file format, the bot will send a warning message.

Here's a clear and detailed documentation section for the **Weight Filter**, based on the entries in your provided `filters.yaml`:

---

## Filters

Each filter contains several common paramenters:
* **`quarantineWeight`**: Score contribution toward quarantine the user. (Should be lower than banWeight)
* **`banWeight`**: Score contribution toward banning the user.
* **`inputTransformer`**: Preprocesses the message before evaluation (e.g., `remove_unicode`, `lowercase`).

When a user sends a message:

1. The input is transformed according to the filter’s rules.
2. The message is checked against the filter.
3. If a match is found, the filter contributes to the total **quarantine** or **ban** score.
4. If the total score crosses the defined threshold for action, the bot responds accordingly.
5. Filters invoked one by one in chain, until first of them will return **ban** score. Otherwise in the end the message can be quarantined if **quarantine** score acheived.

---

## ⚖️ Weight Filters

**Weight filters** evaluate incoming messages against a list of keywords or regex patterns. If a message matches one or more patterns, it accumulates a **score**, and actions (quarantine or ban) are taken based on threshold values.

This system allows flexible, layered spam detection based on the content severity.

`weight` filter includes:

* **`restrictionPatterns`**: A list of regex patterns or keywords to detect.

---

### 📂 Example: Common Words Filter

```yaml
type: weight
name: 'Spam words'
restrictionPatterns:
  - 'income'
  - 'bitcoin'
  - 'money'
  - 'remote'
  - 'hiring'
  - 'sms'
  - '\\d{3,}'
quarantineWeight: 3
banWeight: 5
inputTransformer:
  type: combine
  transformers:
    - type: remove_unicode
    - type: lowercase
```

This filter will:

* Preprocess messages by removing Unicode noise and converting text to lowercase.
* Match messages containing any of the specified recruitment or spam keywords.
* If the message will contains 3 of those matches it will be quarantined or banned if reach 5
