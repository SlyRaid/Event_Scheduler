# event Scheduler

## Основные компоненты
Task.kt — модель задачи.

TaskRepository.kt / PostgresTaskRepository.kt — интерфейс и реализация репозитория задач.

TaskScheduler.kt — логика выполнения запланированных задач.

Routing.kt — HTTP-маршруты.

Databases.kt — конфигурация подключения к БД.

Serialization.kt / UseSerializers.kt — настройки сериализации.

## Логирование
Конфигурируется через logback.xml, логи сохраняются в logs/task-scheduler.log.


## db
![image](https://github.com/user-attachments/assets/99f25473-a997-45be-8b32-82346fd7125d)
