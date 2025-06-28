Here is your final **`README.md`** for the SpamFighters project, complete and production-ready:

# 🛡️ SpamFighters – Open-Source Telegram Antispam Bot

A powerful, modular anti-spam bot for Telegram with customizable filtering strategies per chat.  
Built for communities, powered by AI and flexible rules.

---

## 📚 Table of Contents

- [⚙️ Setup Steps](#️-setup-steps)
  - [1. Create a Log Group](#1-create-a-log-group)
  - [2. Get the Chat ID](#2-get-the-chat-id)
  - [3. Add the Bot to Your Main Chat](#3-add-the-bot-to-your-main-chat)
  - [4. Enable the Bot](#4-enable-the-bot)
  - [5. Set the Log Chat ID](#5-set-the-log-chat-id)
- [🧪 Setup Filters](#-setup-filters)
- [📥 Upload Filter Configuration](#-upload-filter-configuration)
- [🔍 Filters](#-filters)
  - [🌐 Language Injection Detection](#-language-injection-detection)
  - [😊 Emoji Limit Filter](#-emoji-limit-filter)
  - [🤖 AI Spam Model Filter](#-ai-spam-model-filter)
  - [💰 Strong Restricted Words](#-strong-restricted-words)
- [🧾 Example: Full `filters.yaml`](#-example-full-filtersyaml)

---

## ⚙️ Setup Steps

Follow these steps to get your Telegram anti-spam bot up and running:

### 1. Create a Log Group

- Create a **Telegram group** that will serve as the **log/admin chat**.
- Add [`@spam_eater_bot`](https://t.me/spam_eater_bot) to the group.
- Grant the bot **admin permissions**:
  - Set Admin Permissions

This group will receive logs and alerts about spam detection and actions taken.

### 2. Get the Chat ID

Send the following command in the log group:

```text
/get_chat_id
````

* The bot will reply with the group’s ID.
* **Copy this value** — you'll need it in the next steps.

### 3. Add the Bot to Your Main Chat

* Invite `@spam_eater_bot` to the Telegram group you want to protect.
* Grant it **admin permissions**:

  * Can delete messages
  * Can restrict members
  * Can read all messages
  * Add Admin Permission

### 4. Enable the Bot

In your **main chat**, activate the bot:

```text
/enable
```

### 5. Set the Log Chat ID

Back in your main chat, link the log group:

```text
/set_admin_chat_id <your_log_chat_id>
```

Example:

```text
/set_admin_chat_id -1001234567890
```

✅ The bot is now actively protecting your group and logging actions to the admin group.

---

## 🧪 Setup Filters

You can configure the bot using custom filters defined in a YAML file.

## 📥 Upload Filter Configuration

1. Open the **log/admin chat**.
2. Upload your `filters.yaml` file as a **document** (not text/photo).

The bot will:

* ✅ Confirm successful loading.
* ❌ Warn if the file is invalid.

---

## 🔍 Filters

Each filter includes common parameters:

* `enabled`: Enables/disables the filter.
* `quarantineWeight`: Contributes to the user’s quarantine score.
* `banWeight`: Contributes to the user’s ban score.
* `inputTransformer`: Preprocesses the message (e.g., `lowercase`, `remove_unicode`).

### Evaluation Logic

1. The message is transformed.
2. Filters are applied sequentially.
3. Scores accumulate.
4. If a ban score is reached, the message is blocked.
5. If only a quarantine threshold is met, the user is quarantined.

---

### 🌐 Language Injection Detection

```yaml
type: language_injection
name: 'Injection in English words'
strictLanguagePattern: '[A-Za-z]'
quarantineWeight: 2
banWeight: 3
inputTransformer:
  type: remove_unicode
```

---

### 😊 Emoji Limit Filter

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

---

### 🤖 AI Spam Model Filter

```yaml
type: remote_filter
name: 'AI Spam Model'
endpoint: https://your.api.endpoint/check
minMessageLengthForCheck: 40
enabled: true
quarantineWeight: 0.3
banWeight: 0.98
inputTransformer:
  type: pass
```

---

### 💰 Strong Restricted Words

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

---

## 🧾 Example: Full `filters.yaml`

```yaml
filters:
  - type: language_injection
    strictLanguagePattern: '[A-Za-z]'
    name: Injection in English words
    quarantineWeight: 2
    banWeight: 3
    inputTransformer:
      type: remove_unicode

  - type: weight
    name: Emoji Limit
    restrictionPatterns:
      - '[^\p{L}\p{M}\p{N}\p{P}\p{Z}\p{Cf}\p{Cs}\s[!-/][:-@][\[-`][{-~]]'
    quarantineWeight: 5
    banWeight: 10
    inputTransformer:
      type: pass

  - type: remote_filter
    endpoint: 'https://your.api.endpoint/check'
    minMessageLengthForCheck: 40
    name: AI Spam Model
    enabled: true
    quarantineWeight: 0.3
    banWeight: 0.98
    inputTransformer:
      type: pass

  - type: weight
    name: Restricted words
    restrictionPatterns:
      - '\\d+\\s*[$]'
      - '[$]\\s*\\d+'
      - '18\\s*[+]'
      - '\\s\\d{3}k'
      - 'usd'
      - 'eur'
    quarantineWeight: 1
    banWeight: 2
    inputTransformer:
      type: combine
      transformers:
        - type: remove_unicode
        - type: lowercase
```
