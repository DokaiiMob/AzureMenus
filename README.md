<div align="center">
  <h1>🌊 AzureMenus</h1>
  <p><strong>Мощный и гибкий плагин для создания интерактивных меню в Minecraft</strong></p>
  
  <img src="https://img.shields.io/badge/Minecraft-1.21+-green.svg" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/Java-21+-orange.svg" alt="Java Version">
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License">
  <img src="https://img.shields.io/badge/Platform-Spigot%2FPaper-red.svg" alt="Platform">
</div>

## 📋 Описание

AzureMenus — это современный плагин для серверов Minecraft, который позволяет создавать красивые и функциональные интерактивные меню через простые YAML файлы. Плагин поддерживает MiniMessage форматирование, систему действий, условную логику и многое другое.

### ✨ Ключевые особенности

- 🎨 **MiniMessage поддержка** — градиенты, цвета, форматирование текста
- 🔧 **Гибкая система действий** — команды, сообщения, закрытие меню
- 📊 **PlaceholderAPI интеграция** — динамический контент
- 🎭 **Условная логика** — показ элементов по условиям
- 🔊 **Звуковые эффекты** — настраиваемые звуки для взаимодействий
- ⚡ **Автообновление меню** — динамическое обновление контента
- 🛡️ **Система разрешений** — контроль доступа к элементам
- 📁 **Простая конфигурация** — интуитивные YAML файлы

## 🚀 Установка

1. **Скачайте** последнюю версию плагина из [релизов](https://github.com/DokaiiMob/AzureMenus/releases)
2. **Поместите** JAR файл в папку `plugins` вашего сервера
3. **Перезапустите** сервер
4. **Настройте** меню в папке `plugins/AzureMenus/menus/`

### 📋 Требования

- Minecraft сервер 1.21+
- Java 21+
- Spigot/Paper сервер
- (Опционально) PlaceholderAPI для плейсхолдеров

## 📖 Использование

### Основные команды

```
/azuremenus reload          - Перезагрузить конфигурацию и меню
/azuremenus open <menu>     - Открыть меню для игрока
/azuremenus list            - Показать список всех меню
```

### Создание меню

Создайте YAML файл в папке `plugins/AzureMenus/menus/`. Пример базового меню:

```yaml
title: '<gradient:#00BFFF:#8A2BE2>Главное меню</gradient>'
size: 27
sound: 'UI_BUTTON_CLICK'
update-interval: 100

# Заполнение пустых слотов
fill:
  enabled: true
  material: 'GRAY_STAINED_GLASS_PANE'
  name: ' '

items:
  example_item:
    slot: 13
    material: 'DIAMOND'
    name: '<gradient:#FFD700:#FFA500>Пример предмета</gradient>'
    lore:
      - '<gray>Это пример предмета в меню</gray>'
      - '<yellow>Нажмите для взаимодействия</yellow>'
    actions:
      - type: 'message'
        message: '<green>Привет, %player%!</green>'
      - type: 'command'
        commands:
          - 'gamemode creative %player%'
```

### Конфигурация предметов

#### Основные параметры

```yaml
items:
  item_id:
    slot: 0                    # Слот в инвентаре (0-53)
    material: 'DIAMOND'        # Материал предмета
    amount: 1                  # Количество
    name: 'Название'           # Отображаемое имя
    lore:                      # Описание предмета
      - 'Строка 1'
      - 'Строка 2'
    enchanted: true            # Эффект зачарования
    item-flags:                # Флаги предмета
      - 'HIDE_ATTRIBUTES'
    condition: '%player% == admin'  # Условие показа
```

#### Система действий

**Отправка сообщения:**
```yaml
actions:
  - type: 'message'
    message: '<green>Привет, %player%!</green>'
    click-types: ['LEFT', 'RIGHT']  # Типы кликов
    permission: 'menu.message'      # Требуемое разрешение
```

**Выполнение команд:**
```yaml
actions:
  - type: 'command'
    commands:
      - 'tp %player% spawn'
      - 'give %player% diamond 1'
    executor: 'CONSOLE'  # CONSOLE или PLAYER
    async: false         # Асинхронное выполнение
```

**Закрытие меню:**
```yaml
actions:
  - type: 'close'
```

### Использование плейсхолдеров

Плагин поддерживает PlaceholderAPI и внутренние плейсхолдеры:

```yaml
name: '<yellow>Игрок: %player%</yellow>'
lore:
  - '<gray>Онлайн: %server_online%</gray>'
  - '<gray>Баланс: %vault_eco_balance%</gray>'
```

## 🎨 Примеры меню

### Главное меню RPG сервера

В проекте включены примеры меню:

- `main.yml` — главное меню с основными разделами
- `teleports.yml` — меню телепортации по локациям

### Кастомизация

```yaml
# Настройка цветов и эффектов
title: |
  <gradient:#FF6B6B:#4ECDC4>🏰 Королевство AzureMyst</gradient>
  <gray>❖</gray> <white>Выберите действие</white>

# Анимированные элементы
items:
  animated_gem:
    slot: 22
    material: 'EMERALD'
    name: |
      <animation:rainbow>💎 Магический кристалл 💎</animation>
    enchanted: true
```

## ⚙️ Конфигурация

### config.yml

```yaml
# Основные настройки
debug: false                    # Отладочная информация
language: 'ru'                  # Язык интерфейса
update-check: true              # Проверка обновлений

# Настройки меню
menu:
  close-on-click-outside: true  # Закрывать при клике вне меню
  sound-enabled: true           # Звуковые эффекты
  animation-enabled: true       # Анимации

# Интеграция с другими плагинами
integrations:
  placeholderapi: true          # PlaceholderAPI поддержка
  vault: true                   # Vault интеграция
```

## 🔧 API для разработчиков

### Maven зависимость

```xml
<dependency>
    <groupId>dev.azuremyst</groupId>
    <artifactId>azuremenus</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

### Использование API

```java
// Получение менеджера меню
MenuManager menuManager = AzureMenusPlugin.getInstance().getMenuManager();

// Открытие меню для игрока
menuManager.openMenu(player, "main");

// Создание кастомного действия
ActionRegistry.registerFactory("custom", (config) -> {
    return new CustomAction(config);
});
```

## 🤝 Поддержка

- **Discord:** [Присоединиться к серверу](https://discord.gg/yourdiscord)
- **Issues:** [Создать баг-репорт](https://github.com/DokaiiMob/AzureMenus/issues)
- **Wiki:** [Документация](https://github.com/DokaiiMob/AzureMenus/wiki)

## 📄 Лицензия

Этот проект лицензирован под MIT License - см. файл [LICENSE](LICENSE) для деталей.

## 🙏 Благодарности

- **MiniMessage** за отличную систему форматирования текста
- **PlaceholderAPI** за универсальную систему плейсхолдеров
- **Spigot/Paper** команды за отличную платформу

---

<div align="center">
  <p>Сделано с ❤️ для сообщества Minecraft</p>
  <p>© 2025 AzureMyst Development</p>
</div>

# AzureMenus

> Advanced menu system for AzureMyst Minecraft server

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0--SNAPSHOT-blue)
![Minecraft](https://img.shields.io/badge/minecraft-1.21.4-green)
![Java](https://img.shields.io/badge/java-21-orange)
![License](https://img.shields.io/badge/license-MIT-green)

**✨ Современная система меню для Minecraft серверов ✨**

</div>

## 🚀 Особенности

- **🎨 Современные градиенты** - Поддержка HEX цветов и градиентов в стиле AzureMyst
- **🌍 Локализация** - Полная поддержка русского языка из коробки
- **⚡ Высокая производительность** - Оптимизированная работа с меню
- **🔧 Гибкая настройка** - Простая конфигурация через YAML
- **🎯 Система действий** - Мощная система действий для интерактивности
- **📱 Интеграции** - Поддержка PlaceholderAPI, Vault, AuraSkills, KingdomsX
- **🎭 Анимации** - Поддержка анимированных элементов меню
- **🔒 Права доступа** - Детальная система разрешений

## 📋 Требования

- **Minecraft**: 1.21.4
- **Ядро**: Paper/Purpur/Spigot
- **Java**: 21+

## 🛠 Установка

1. Скачайте `AzureMenus-1.0.0.jar` из releases
2. Поместите в папку `plugins/` вашего сервера
3. Перезапустите сервер
4. Настройте конфигурацию в `plugins/AzureMenus/`

## 📖 Конфигурация

### config.yml
```yaml
# Основной язык
language: 'ru'

# Тема AzureMyst
azure-theme:
  primary-color: '#8A2BE2'    # Фиолетовый
  secondary-color: '#00BFFF'  # Голубой
```

### Создание меню
Создайте файл `plugins/AzureMenus/menus/example.yml`:

```yaml
title: '<gradient:#8A2BE2:#00BFFF>Пример меню</gradient>'
size: 27

items:
  info:
    slot: 13
    material: 'DIAMOND'
    name: '<gradient:#8A2BE2:#00BFFF>Информация</gradient>'
    lore:
      - '<white>Добро пожаловать на AzureMyst!</white>'
      - '<gray>Игроков онлайн: <white>%server_online%</white></gray>'
    actions:
      - type: 'message'
        message: '<green>Привет, %player_name%!</green>'
```

## 🎮 Команды

| Команда | Описание | Права |
|---------|----------|-------|
| `/azuremenus` | Главная команда | `azure.menus.*` |
| `/azuremenus reload` | Перезагрузить плагин | `azure.menus.reload` |
| `/azuremenus open <menu> [player]` | Открыть меню | `azure.menus.open.*` |
| `/azuremenus list` | Список меню | `azure.menus.list` |

## 🔑 Права доступа

```yaml
azure.menus.*           # Полный доступ (ops)
azure.menus.reload      # Перезагрузка (ops)
azure.menus.open.*      # Открытие любого меню
azure.menus.list        # Просмотр списка меню
```

## 🎨 Цветовая палитра AzureMyst

- **Основной**: `#8A2BE2` (BlueViolet)
- **Дополнительный**: `#00BFFF` (DeepSkyBlue)
- **Акцент**: `#FF6347` (Tomato)
- **Успех**: `#32CD32` (LimeGreen)
- **Предупреждение**: `#FFD700` (Gold)
- **Ошибка**: `#FF4500` (OrangeRed)

## 📚 API

### Открытие меню через API
```java
MenuManager menuManager = AzureMenusPlugin.getInstance().getMenuManager();
menuManager.openMenu(player, "menu-name");
```

### Создание градиента
```java
String gradient = ColorUtil.createGradient("#8A2BE2", "#00BFFF", "Текст");
```

## 🔌 Интеграции

- **PlaceholderAPI** - Поддержка всех плейсхолдеров
- **Vault** - Экономика и права доступа
- **AuraSkills** - Интеграция с системой навыков
- **KingdomsX** - Поддержка королевств

## 🐛 Баг-репорты и предложения

Если вы нашли баг или у вас есть предложения по улучшению:
1. Создайте issue на GitHub
2. Опишите проблему детально
3. Приложите логи сервера
4. Укажите версию плагина

## 🤝 Содействие

Мы приветствуем ваш вклад! Для начала:
1. Fork репозитория
2. Создайте ветку для функции
3. Сделайте изменения
4. Создайте Pull Request

## 📜 Лицензия

Этот проект распространяется под лицензией MIT. Подробности в файле [LICENSE](LICENSE).

## 👥 Авторы

- **AzureMyst Team** - *Основная разработка*

---

<div align="center">

**Сделано с ❤️ для сообщества AzureMyst**

[🌐 Сайт](https://azuremyst.dev) • [💬 Discord](https://discord.gg/azuremyst) • [📖 Wiki](https://wiki.azuremyst.dev)

</div>
