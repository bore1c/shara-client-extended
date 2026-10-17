# Shara Client Extended

Простой Fabric клиент с базовыми чит-модулями: ESP (список nearby entities), Killaura, X-Ray (toggle), AutoArmor (простая логика), AutoClicker, HUD с отображением включённых модулей и системой конфигурации на Gson (config в %appdata%/.minecraft/config/shara-client-extended.json).

Сборка

1) Локально: если есть gradle, просто запустить:
   gradle build

2) Или создать Gradle Wrapper и потом:
   ./gradlew build   (UNIX)
   gradlew.bat build (Windows)

Результирующий JAR будет в build/libs/ под именем shara-client-extended-0.1.0.jar

Использование

Положите JAR в папку %appdata%/.minecraft/mods вместе с Fabric API и запустите профиль fabric-loader-1.21.4.

Важное замечание

Этот репозиторий содержит образовательный пример. Использование чита на публичных серверах может привести к бану. Я не несу ответственности за последствия использования.
