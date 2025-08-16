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
