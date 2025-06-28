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

## 🔍 Filters

Each filter contains several common paramenters:
* **`enable`**: Enable or disable the filter
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

Here’s a breakdown of the filters defined in your `filters.yaml`, formatted for your documentation. Each filter is categorized and described with clear YAML references and explanations.

---

### 1. 🌐 **Language Injection Detection** (`language_injection` filter example)

```yaml
type: language_injection
name: 'Injection in russian words'
strictLanguagePattern: '[A-Za-z]'
quarantineWeight: 2
banWeight: 3
inputTransformer:
  type: remove_unicode
```

* Detects messages that mix non-Latin symbols into Engligh text.
* Helps stop injection-style spam using obfuscated characters.

---

### 2. 😊 **Emoji Limit Filter** (`weight` filter example for emoji)

```yaml
type: weight
name: 'Emoji Limit'
restrictionPatterns:
  - '[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s[!-/][:-@][\\[-`][{-~]]'
quarantineWeight: 5
banWeight: 10
inputTransformer:
  type: pass
```

* Detects excessive or suspicious use of emojis or non-standard Unicode.
* High score leads to direct banning due to likely spam nature.

---

### 3. 🤖 **AI Spam Model Filter** (`remote_filter` filter example)

```yaml
type: remote_filter
name: 'AI Spam Model'
endpoint: <Your remote API endpoint>
minMessageLengthForCheck: 40
enabled: true
quarantineWeight: 0.3
banWeight: 0.98
inputTransformer:
  type: pass
```

* Sends longer messages to an external AI spam detection API.
* Contributes partial weights depending on classification.

---

### 4. 💰 **Strong Restricted Words** (`weight` filter example)

```yaml
type: weight
name: 'Strong restricted words'
restrictionPatterns:
  - '\\d+\\s*[$]'
  - '[$]\\s*\\d+'
  - '18\\s*[+]'
  - '\\s\\d{3}k'
  - 'usd'
  - 'eur'
  - 'income'
  - 'bitcoin'

quarantineWeight: 1
banWeight: 2
inputTransformer:
  type: combine
  transformers:
    - type: remove_unicode
    - type: lowercase
```

* Matches explicit monetary patterns and aggressive financial bait.
* Minimal weight allows stacking with other filters for high accuracy.

### 🧾 Example of `filters.yaml` file

```
filters:
  - type: language_injection
    strictLanguagePattern: '[A-Za-Z]'
    name: Injection in english words
    quarantineWeight: 2
    banWeight: 3
    inputTransformer:
      type: remove_unicode
  - type: weight
    restrictionPatterns:
      - '[^\p{L}\p{M}\p{N}\p{P}\p{Z}\p{Cf}\p{Cs}\s[!-/][:-@][\[-`][{-~]]'
    name: Emoji Limit
    quarantineWeight: 5
    banWeight: 10
    inputTransformer:
      type: pass
  - type: remote_filter
    endpoint: 'https://my.domain/endpoint'
    minMessageLengthForCheck: 40
    name: AI Spam Model
    enabled: false
    quarantineWeight: 0.3
    banWeight: 0.98
    inputTransformer:
      type: pass
  - type: weight
    restrictionPatterns:
      - '\d+\s*[$]'
      - '[$]\s*\d+'
      - '18\s*[+]'
      - '\s\d{3}k'
      - usd
      - eur
    name: Restricted words
    quarantineWeight: 1
    banWeight: 2
    inputTransformer:
      type: combine
      transformers:
        - type: remove_unicode
        - type: lowercase
```
