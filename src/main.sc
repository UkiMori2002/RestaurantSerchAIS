require: slotfilling/slotFilling.sc
  module = sys.zb-common

theme: /

    state: Start
        q!: $regex</start>
        a: Привет! Я помогу вам найти ресторан, бар или кафе. Давайте начнём.
        intent: /привет || toState = "/Hello"

    state: Hello
        intent!: /привет
        a: Какой тип заведения вы ищете? (ресторан, бар, кафе)
        intent: /FindRestaurant || toState = "/Запрос типа заведения"
        event: noMatch || toState = "./"

    state: Запрос типа заведения
        a: Какой тип заведения вы ищете? (ресторан, бар, кафе)
        intent: /FindRestaurant || toState = "/Определение типа заведения"

    state: Определение типа заведения
        intent!: /FindRestaurant
        script:
            // Извлекаем тип заведения из текста пользователя
            $session.establishmentType = $parseTree._Type; 
        if: $session.establishmentType == undefined
            a: Я не понял. Вы сказали: {{$request.query}}. Пожалуйста, выберите из списка: ресторан, бар, кафе.
            go!: /Запрос типа заведения
        else: 
            a: Вы ищете {{$session.establishmentType}}. В каком районе вы хотите найти заведение?
            go!: /Запрос района
            
    # state: Запрос района
    #     a: В каком районе вы хотите найти заведение?
    #     buttons:
    #         "Центр" -> /Определение района
    #         "Кировский район" -> /Определение района
    #         "Окраина" -> /Определение района
    #     intent: /AreaRequest || toState = "/Определение района"
    #     event: noMatch || toState = "./"

    # state: Определение района
    #     intent!: /AreaRequest
    #     script:
    #         // Если значение передано через кнопку, извлекаем его из $context.query
    #         if ($context.query == "Центр") {
    #             $session.area = "центр";
    #         } else if ($context.query == "Кировский район") {
    #             $session.area = "кировский район";
    #         } else if ($context.query == "Окраина") {
    #             $session.area = "окраина";
    #         } else {
    #             // Если значение передано через текст, извлекаем его
    #             $session.area = $parseTree._Area;
    #         }
    #     if: $session.area == undefined
    #         a: Я не понял. Вы сказали: {{$request.query}}
    #         go!: /Запрос района
    #     else: 
    #         a: Вы выбрали район {{$session.area}}. Какую кухню вы предпочитаете?
    #         go!: /Запрос кухни
    #     event: noMatch || toState = "./"

    # state: Запрос кухни
    #     a: Какую кухню вы предпочитаете? (итальянская, японская, европейская)
    #     buttons:
    #         "Итальянская" -> /Определение кухни
    #         "Японская" -> /Определение кухни
    #         "Европейская" -> /Определение кухни
    #     intent: /CuisineRequest || toState = "/Определение кухни"
    #     event: noMatch || toState = "./"

    # state: Определение кухни
    #     intent!: /CuisineRequest
    #     script:
    #         // Если значение передано через кнопку, извлекаем его из $context.query
    #         if ($context.query == "Итальянская") {
    #             $session.cuisine = "итальянская";
    #         } else if ($context.query == "Японская") {
    #             $session.cuisine = "японская";
    #         } else if ($context.query == "Европейская") {
    #             $session.cuisine = "европейская";
    #         } else {
    #             // Если значение передано через текст, извлекаем его
    #             $session.cuisine = $parseTree._Cuisine;
    #         }
    #     if: $session.cuisine == undefined
    #         a: Я не понял. Вы сказали: {{$request.query}}
    #         go!: /Запрос кухни
    #     else: 
    #         a: Вы выбрали кухню {{$session.cuisine}}. Какой бюджет вы рассматриваете?
    #         go!: /Запрос бюджета
    #     event: noMatch || toState = "./"

    # state: Запрос бюджета
    #     a: Какой бюджет вы рассматриваете? (дешёвый, средний, дорогой)
    #     buttons:
    #         "Дешёвый" -> /Определение бюджета
    #         "Средний" -> /Определение бюджета
    #         "Дорогой" -> /Определение бюджета
    #     intent: /BudgetRequest || toState = "/Определение бюджета"
    #     event: noMatch || toState = "./"

    # state: Определение бюджета
    #     intent!: /BudgetRequest
    #     script:
    #         // Если значение передано через кнопку, извлекаем его из $context.query
    #         if ($context.query == "Дешёвый") {
    #             $session.budget = "дешёвый";
    #         } else if ($context.query == "Средний") {
    #             $session.budget = "средний";
    #         } else if ($context.query == "Дорогой") {
    #             $session.budget = "дорогой";
    #         } else {
    #             // Если значение передано через текст, извлекаем его
    #             $session.budget = $parseTree._Budget;
    #         }
    #     if: $session.budget == undefined
    #         a: Я не понял. Вы сказали: {{$request.query}}
    #         go!: /Запрос бюджета
    #     else: 
    #         a: Вы выбрали бюджет {{$session.budget}}. Ищу подходящие заведения...
    #         go!: /Поиск заведений
    #     event: noMatch || toState = "./"

    # state: Поиск заведений
    #     script:
    #         // Формируем запрос к API 2GIS
    #         $temp.apiUrl = "https://catalog.api.2gis.com/3.0/items";
    #         $temp.params = {
    #             q: $session.establishmentType, // Тип заведения
    #             region_id: 32, // Регион (например, Иркутск)
    #             key: "8933d123-6a66-4a48-8945-278c2b19bd5b", // Ваш API-ключ
    #             sort_point: "104.2802,52.2864", // Координаты для поиска
    #             fields: "items.point,items.name,items.type,items.cuisine,items.rating,items.average_bill"
    #         };

    #         // Отправляем GET-запрос
    #         $temp.response = $http.get($temp.apiUrl, { params: $temp.params });

    #     if: $temp.response.isOk && $temp.response.data.result && $temp.response.data.result.items.length > 0
    #         script:
    #             $temp.establishmentMessages = "";
    #             $temp.index = 0;
    #             while ($temp.index < $temp.response.data.result.items.length) {
    #                 $temp.item = $temp.response.data.result.items[$temp.index];
    #                 $temp.establishmentMessages += "---\n";
    #                 $temp.establishmentMessages += "Название: " + $temp.item.name + "\n";
    #                 $temp.establishmentMessages += "Тип: " + $temp.item.type + "\n";
    #                 $temp.establishmentMessages += "Кухня: " + ($temp.item.cuisine || "не указана") + "\n";
    #                 $temp.establishmentMessages += "Рейтинг: " + ($temp.item.rating || "не указан") + "\n";
    #                 $temp.establishmentMessages += "Средний чек: " + ($temp.item.average_bill || "не указан") + "\n";
    #                 $temp.index++;
    #             }
    #         a: |
    #             Вот подходящие заведения:
    #             {{$temp.establishmentMessages}}
    #     else: 
    #         a: Не удалось найти заведения по вашим параметрам. Попробуйте ещё раз.

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}} .
        go!: /Start

    state: Bye
        intent!: /пока
        a: До свидания! Если понадобится помощь, обращайтесь.

    state: Подтверждение данных
        a: Ваши данные:
                Тип заведения: {{$session.establishmentType}}
                Район: {{$session.area}}
                Кухня: {{$session.cuisine}}
                Бюджет: {{$session.budget}}
                Подтвердите поиск или начните заново.
        buttons:
            "Подтвердить" -> /Поиск заведений
            "Начать с начала" -> /Start