# 🚀 Инструкция по загрузке на GitHub

## Шаги для создания репозитория и загрузки кода:

### 1. Создание репозитория на GitHub
1. Перейдите на [github.com](https://github.com)
2. Войдите в свой аккаунт
3. Нажмите кнопку "New" для создания нового репозитория
4. Заполните данные:
   - **Repository name:** `AzureMenus`
   - **Description:** `🌊 Мощный и гибкий плагин для создания интерактивных меню в Minecraft`
   - **Visibility:** Public (или Private по желанию)
   - ❌ НЕ добавляйте README, .gitignore или LICENSE (они уже есть в проекте)

### 2. Настройка удаленного репозитория
```bash
# Если репозиторий уже создан, замените URL на ваш
git remote set-url origin https://github.com/ВАШ_USERNAME/AzureMenus.git

# Или добавьте новый origin, если его ещё нет
git remote add origin https://github.com/ВАШ_USERNAME/AzureMenus.git
```

### 3. Загрузка кода
```bash
# Переименуем ветку в main (современный стандарт)
git branch -M main

# Загружаем код на GitHub
git push -u origin main
```

### 4. Создание первого релиза
1. Перейдите в ваш репозиторий на GitHub
2. Нажмите на вкладку "Releases"
3. Нажмите "Create a new release"
4. Заполните данные:
   - **Tag version:** `v1.0.0`
   - **Release title:** `🌊 AzureMenus v1.0.0 - Первый релиз`
   - **Description:** См. ниже

### 5. Описание первого релиза (Release Notes)

```markdown
## 🌊 AzureMenus v1.0.0 - Первый релиз

Первая стабильная версия плагина AzureMenus для создания интерактивных меню в Minecraft!

### ✨ Основные возможности

- 🎨 **MiniMessage поддержка** — красивые градиенты и цвета
- 🔧 **Гибкая система действий** — команды, сообщения, закрытие меню  
- 📊 **PlaceholderAPI интеграция** — динамический контент
- 🎭 **Условная логика** — показ элементов по условиям
- 🔊 **Звуковые эффекты** — настраиваемые звуки
- ⚡ **Автообновление меню** — динамическое обновление контента
- 🛡️ **Система разрешений** — контроль доступа к элементам
- 📁 **YAML конфигурация** — простая настройка

### 📋 Требования

- Minecraft 1.21+
- Java 21+
- Spigot/Paper сервер
- (Опционально) PlaceholderAPI

### 📦 Установка

1. Скачайте `AzureMenus-1.0.0.jar` из Assets ниже
2. Поместите в папку `plugins/` вашего сервера
3. Перезапустите сервер
4. Настройте меню в `plugins/AzureMenus/menus/`

### 🎮 Команды

- `/azuremenus reload` - Перезагрузить конфигурацию
- `/azuremenus open <menu>` - Открыть меню
- `/azuremenus list` - Список всех меню

### 📚 Документация

Полная документация доступна в [README.md](https://github.com/ВАШ_USERNAME/AzureMenus/blob/main/README.md)

### 🗺️ Планы на будущее

Ознакомьтесь с нашими планами в [ROADMAP.md](https://github.com/ВАШ_USERNAME/AzureMenus/blob/main/ROADMAP.md)

### 🐛 Сообщить о проблеме

Если вы нашли баг, пожалуйста, [создайте issue](https://github.com/ВАШ_USERNAME/AzureMenus/issues)

---

**Спасибо за использование AzureMenus! 🙏**
```

### 6. Загрузка JAR файла
1. В процессе создания релиза, в разделе "Assets"
2. Перетащите файл `target/azuremenus-1.0.0-SNAPSHOT.jar` 
3. Переименуйте его в `AzureMenus-1.0.0.jar`
4. Нажмите "Publish release"

## 🎯 Готовая структура проекта

Ваш репозиторий будет содержать:

```
AzureMenus/
├── 📁 src/main/
│   ├── 📁 java/dev/azuremyst/azuremenus/
│   └── 📁 resources/
├── 📄 pom.xml
├── 📄 README.md (подробная документация)
├── 📄 ROADMAP.md (планы развития)
├── 📄 LICENSE (MIT лицензия)
├── 📄 .gitignore
└── 📄 GITHUB_SETUP.md (эта инструкция)
```

## ✅ После загрузки

1. ✅ Добавьте топики к репозиторию: `minecraft`, `spigot`, `paper`, `menu`, `gui`
2. ✅ Добавьте описание репозитория
3. ✅ Включите Issues и Wiki в настройках репозитория
4. ✅ Создайте релиз v1.0.0 с JAR файлом
5. ✅ Поделитесь ссылкой на репозиторий с сообществом!

---

🌊 **Удачной разработки с AzureMenus!**
