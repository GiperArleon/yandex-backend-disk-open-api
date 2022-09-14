# yandex-backend-disk-open-api
Тестовое задание в школу бэкенд разработки Yandex 2022 (Бэкенд для веб-сервиса хранения файлов).<br />
Реализованы апи методы:<br />
## /imports
POST запрос.<br />
Импортирует элементы файловой системы. Элементы импортированные повторно обновляют текущие.<br />
Пример: https://convenience-1972.usr.yandex-academy.ru/imports
```
{
    "items": [
        {
            "type": "FOLDER",
            "id": "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            "parentId": null
        }
    ],
    "updateDate": "2022-02-01T12:00:00Z"
}
```
Пример успешного ответа:<br />
200
```
```
Невалидная схема документа или входные данные не верны:<br />
400
```
   {
    "code": 400,
    "message": "Validation Failed"
   }
```
## /delete/{id}
DELETE запрос.<br />
Удалить элемент по идентификатору. При удалении папки удаляются все дочерние элементы. Доступ к истории обновлений удаленного элемента невозможен.<br />
Пример: https://convenience-1972.usr.yandex-academy.ru/delete/069cb8d7-bbdd-47d3-ad8f-82ef4c269df1
<br />Пример успешного ответа:<br />
200
```
```
Невалидная схема документа или входные данные не верны:<br />
400
```
   {
    "code": 400,
    "message": "Validation Failed"
   }
```
Элемент не найден:<br />
404
```
   {
    "code": 404,
    "message": "Item not found"
   }
```
## /nodes/{id}
GET запрос.<br />
Получить информацию об элементе по идентификатору. При получении информации о папке также предоставляется информация о её дочерних элементах.<br />
Пример: https://convenience-1972.usr.yandex-academy.ru/nodes/069cb8d7-bbdd-47d3-ad8f-82ef4c269df1
<br />Пример успешного ответа:<br />
200
```
{
    "id": "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
    "url": null,
    "date": "2022-02-03T12:00:00Z",
    "parentId": null,
    "type": "FOLDER",
    "size": 1920,
    "children": [
        {
            "id": "d515e43f-f3f6-4471-bb77-6b455017a2d2",
            "url": null,
            "date": "2022-02-02T12:00:00Z",
            "parentId": "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            "type": "FOLDER",
            "size": 384,
            "children": [
                {
                    "id": "863e1a7a-1304-42ae-943b-179184c077e3",
                    "url": "/file/url1",
                    "date": "2022-02-02T12:00:00Z",
                    "parentId": "d515e43f-f3f6-4471-bb77-6b455017a2d2",
                    "type": "FILE",
                    "size": 128,
                    "children": null
                },
                {
                    "id": "b1d8fd7d-2ae3-47d5-b2f9-0f094af800d4",
                    "url": "/file/url2",
                    "date": "2022-02-02T12:00:00Z",
                    "parentId": "d515e43f-f3f6-4471-bb77-6b455017a2d2",
                    "type": "FILE",
                    "size": 256,
                    "children": null
                }
            ]
        },
        {
            "id": "1cc0129a-2bfe-474c-9ee6-d435bf5fc8f2",
            "url": null,
            "date": "2022-02-03T12:00:00Z",
            "parentId": "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            "type": "FOLDER",
            "size": 1536,
            "children": [
                {
                    "id": "98883e8f-0507-482f-bce2-2fb306cf6483",
                    "url": "/file/url3",
                    "date": "2022-02-03T12:00:00Z",
                    "parentId": "1cc0129a-2bfe-474c-9ee6-d435bf5fc8f2",
                    "type": "FILE",
                    "size": 512,
                    "children": null
                },
                {
                    "id": "74b81fda-9cdc-4b63-8927-c978afed5cf4",
                    "url": "/file/url4",
                    "date": "2022-02-03T12:00:00Z",
                    "parentId": "1cc0129a-2bfe-474c-9ee6-d435bf5fc8f2",
                    "type": "FILE",
                    "size": 1024,
                    "children": null
                }
            ]
        }
    ]
}
```
Невалидная схема документа или входные данные не верны:<br />
400
```
   {
    "code": 400,
    "message": "Validation Failed"
   }
```
Элемент не найден:<br />
404
```
   {
    "code": 404,
    "message": "Item not found"
   }
```
## /updates
GET запрос.<br /> 
Получение списка файлов, которые были обновлены за последние 24 часа включительно [date - 24h, date] от времени переданном в запросе.<br />
Пример запроса за текущий день: https://convenience-1972.usr.yandex-academy.ru/updates
<br />Пример запроса с датой: https://convenience-1972.usr.yandex-academy.ru/updates?date=2022-02-02T12:00:00Z
<br />Пример успешного ответа:<br />
200
```
{
    "items": [
        {
            "id": "863e1a7a-1304-42ae-943b-179184c077e3",
            "url": "/file/url1",
            "parentId": "d515e43f-f3f6-4471-bb77-6b455017a2d2",
            "type": "FILE",
            "size": 128,
            "date": "2022-02-02T12:00:00Z"
        },
        {
            "id": "b1d8fd7d-2ae3-47d5-b2f9-0f094af800d4",
            "url": "/file/url2",
            "parentId": "d515e43f-f3f6-4471-bb77-6b455017a2d2",
            "type": "FILE",
            "size": 256,
            "date": "2022-02-02T12:00:00Z"
        }
    ]
}
```
Нет обновлений за указанный интервал:<br />
200
```
{
    "items": []
}
```
Невалидная схема документа или входные данные не верны:<br />
400
```
   {
    "code": 400,
    "message": "Validation Failed"
   }
```
## /node/{id}/history
GET запрос.<br />
Получение истории обновлений по элементу за заданный полуинтервал [from, to). История по удаленным элементам недоступна.<br />
Пример запроса всей истории: https://convenience-1972.usr.yandex-academy.ru/node/99bc3b31-02d1-4245-ab35-3106c9ee1c65/history
<br />Пример запроса с границей from: https://convenience-1972.usr.yandex-academy.ru/node/99bc3b31-02d1-4245-ab35-3106c9ee1c65/history?dateStart=2022-02-01T00:00:00Z
<br />Пример запроса с границей to: https://convenience-1972.usr.yandex-academy.ru/node/99bc3b31-02d1-4245-ab35-3106c9ee1c65/history?dateEnd=2022-06-03T15:30:00Z
<br />Пример запроса за заданный полуинтервал: https://convenience-1972.usr.yandex-academy.ru/node/99bc3b31-02d1-4245-ab35-3106c9ee1c65/history?dateStart=2022-02-01T00:00:00Z&dateEnd=2022-06-03T15:30:00Z
<br />Пример успешного ответа:<br />
200
   ```
{
    "items": [
        {
            "id": "98883e8f-0507-482f-bce2-2fb306cf6483",
            "url": "/file/url3",
            "parentId": "1cc0129a-2bfe-474c-9ee6-d435bf5fc8f2",
            "type": "FILE",
            "size": 614,
            "date": "2022-02-05T12:00:00Z"
        },
        {
            "id": "98883e8f-0507-482f-bce2-2fb306cf6483",
            "url": "/file/url3",
            "parentId": "1cc0129a-2bfe-474c-9ee6-d435bf5fc8f2",
            "type": "FILE",
            "size": 514,
            "date": "2022-02-04T12:00:00Z"
        },
        {
            "id": "98883e8f-0507-482f-bce2-2fb306cf6483",
            "url": "/file/url3",
            "parentId": "1cc0129a-2bfe-474c-9ee6-d435bf5fc8f2",
            "type": "FILE",
            "size": 512,
            "date": "2022-02-03T12:00:00Z"
        },
        {
            "id": "98883e8f-0507-482f-bce2-2fb306cf6483",
            "url": "/file/url3",
            "parentId": "1cc0129a-2bfe-474c-9ee6-d435bf5fc8f2",
            "type": "FILE",
            "size": 512,
            "date": "2022-02-02T12:00:00Z"
        }
    ]
}
   ```
Невалидная схема документа или входные данные не верны:<br />
400
```
   {
    "code": 400,
    "message": "Validation Failed"
   }
```
Элемент не найден:<br />
404
```
   {
    "code": 404,
    "message": "Item not found"
   }
```
## ci/cd
#### Первая установка
* собрать проект с профилем spring.profiles.active=docker
* собрать образ докера (docker build)
* закинуть образ в docker hub
* подключиться по vpn
* зайти на хост (ssh ubuntu@10.21.3.214)
* создать сеть (sudo docker network create my-network)
* поставить и запустить postgres (sudo docker run --restart=always --net my-network --name yandex6-postgres -e POSTGRES_PASSWORD=1qazse4 -p 5432:5432 -d postgres:alpine)
* зайти в контейнер postgres (sudo docker exec -it 7943a5b3fbf5 /bin/bash)
* зайти в postgres (psql -U postgres -W)
* создать базу CREATE DATABASE files;
* поставить и запустить приложение (sudo docker run -d --restart=always --net my-network -p 80:80 -t giperarleon/yandex-files:0.0.2)
#### Обновление приложения
* собрать проект с профилем spring.profiles.active=docker
* собрать образ докера (docker build)
* закинуть образ в docker hub
* подключиться по vpn
* зайти на хост (ssh ubuntu@10.21.3.214)
* остановить текущее приложение (sudo docker stop 3580e642d932)
* поставить и запустить новую версию приложения (sudo docker run -d --restart=always --net my-network -p 80:80 -t giperarleon/yandex-files:0.0.x)