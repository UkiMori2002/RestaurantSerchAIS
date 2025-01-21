require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        a: Здравствуйте. Я чатбот по поиску ресторанов по вашему предпочтению. 

    state: Hello
        intent!: /привет
        a: Чем я могу вам почочь?  

    state: RecommendByCuisine
        intent!: /найди по кухне
        a: Какую кухню вы хотите? 
        
    state: Bye
        intent!: /пока
        a: До свидания)

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}. Перефразируйте ваш запрос, пожалуйста.

    state: Match
        event!: match
        a: {{$context.intent.answer}}