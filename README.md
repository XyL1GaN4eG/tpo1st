# Лабораторная работа по ТПО #1

## Задание

1. Для указанной функции провести модульное тестирование разложения функции в степенной ряд.
2. Провести модульное тестирование указанного алгоритма с проверкой последовательности попадания в характерные точки.
3. Сформировать доменную модель для заданного текста и разработать тестовое покрытие.

**Вариант:**
- Функция: `arctg(x)`
- Алгоритм: Биномиальная куча
- Предметная область: Брокианский ультра-крикет

---

## 1. Тестирование функции arctg(x)

### Реализация

Функция `arctg(x)` реализована в файле `src/main/kotlin/mymath/Arctg.kt` с использованием степенного ряда:

```
arctg(x) = x - x³/3 + x⁵/5 - x⁷/7 + ...
```

### Особенности реализации

- Для `|x| > 1` используется тождество: `arctg(x) = π/2 - arctg(1/x)`
- Для `x > 0.5` применяется редукция: `arctg(x) = 2 * arctg(x / (1 + √(1 + x²)))`
- Для `0 ≤ x ≤ 0.5` используется прямое вычисление степенного ряда
- Поддержка специальных значений: `NaN`, `±Infinity`, `0`

### Тестовое покрытие

Файл `src/test/kotlin/MathTest.kt`:

| Тест | Описание |
|------|----------|
| `computesArctgUsingPowerSeries` | Проверка корректности вычислений для: 0, ±1, ±√3, ±0.5, ±10 |
| `returnsFiniteValueForFiniteInput` | Проверка конечности результата для конечных входных данных |
| `handlesSpecialValues` | Проверка обработки `Infinity`, `-Infinity`, `NaN` |
| `isOddFunction` | Проверка свойства нечетности: `arctg(-x) = -arctg(x)` |
| `isMonotonic` | Проверка монотонности функции |

---

## 2. Тестирование биномиальной кучи

### Реализация

Биномиальная куча реализована в файле `src/main/kotlin/BinomialQueue.kt`.

### Характерные точки трассировки (TracePoint)

```kotlin
enum class TracePoint {
    ADD_START,                    // Начало добавления элемента
    UNION_START,                  // Начало объединения
    UNION_ADOPT_OTHER_HEAD,       // Принятие головы другой кучи
    UNION_MERGE_ROOT_LISTS,       // Слияние списков корней
    UNION_ADVANCE,                // Продвижение без слияния
    UNION_LINK_NEXT_UNDER_CURRENT,// Связывание next под current
    UNION_LINK_CURRENT_UNDER_NEXT,// Связывание current под next
    EXTRACT_MIN_START,            // Начало извлечения минимума
    EXTRACT_MIN_FOUND_NEW_MIN,    // Найден новый минимум
    EXTRACT_MIN_REMOVE_ROOT,      // Удаление корня
    EXTRACT_MIN_REVERSE_CHILD,    // Обращение списка детей
    EXTRACT_MIN_UNION_CHILDREN,   // Объединение детей
}
```

### Тестовое покрытие

Файл `src/test/kotlin/BinomialQueueTest.kt`:

| Тест                                      | Описание                                               |
|-------------------------------------------|--------------------------------------------------------|
| `keepsMinimumAndSizeAfterInsertions`      | Проверка корректности минимума и размера после вставок |
| `extractMinKeepsSortedOrderAndSize`       | Проверка порядка извлечения (неубывающий)              |
| `removeDeletesSingleMatchingElement`      | Проверка удаления элементов                            |
| `tracesCharacteristicPointsForAdd`        | Трассировка характерных точек при добавлении           |
| `tracesCharacteristicPointsForExtractMin` | Трассировка характерных точек при извлечении           |
| `supportsMutableCollectionContract`       | Проверка контракта `MutableCollection`                 |

### Пример трассировки добавления

Для добавления элемента в кучу `[1]`:
```
ADD_START → UNION_START → UNION_MERGE_ROOT_LISTS → UNION_LINK_NEXT_UNDER_CURRENT
```

Для добавления элемента, когда текущий меньше нового:
```
ADD_START → UNION_START → UNION_MERGE_ROOT_LISTS → UNION_LINK_CURRENT_UNDER_NEXT
```

---

## 3. Доменная модель: Брокианский ультра-крикет

### Предметная область

Много-много миллионов лет назад раса гиперразумных всемерных существ так устала от постоянных споров о смысле жизни, которые отвлекали их от их излюбленного времяпрепровождения — брокианского ультра-крикета (забавная игра, заключающаяся в том, чтобы неожиданно ударить человека без видимой на то причины и убежать).

### Диаграмма классов

```mermaid
direction LR
class BattingGame~P, T~ {
<<Interface>>
  + bat(P, T) Unit
}
class BinomialNode~T~ {
  + BinomialNode(T, Int, BinomialNode~T~?, BinomialNode~T~?, BinomialNode~T~?) 
   BinomialNode~T~? parent
   BinomialNode~T~? sibling
   Int degree
   T key
   BinomialNode~T~? child
}
class BinomialQueue~T~ {
  + BinomialQueue() 
  + contains(T) Boolean
  + addAll(Collection~T~) Boolean
  + extractMin() T?
  + iterator() Iterator~T~
  + retainAll(Collection~T~) Boolean
  - trace(TracePoint) Unit
  + remove(T) Boolean
  + clear() Unit
  - mergeRootLists(BinomialNode~T~?) BinomialNode~T~?
  + add(T) Boolean
  + union(BinomialQueue~T~) Unit
  + removeAll(Collection~T~) Boolean
  + containsAll(Collection~T~) Boolean
  - binomialTreeSize(Int) Int
  - rebuildKeeping((T) -~ Boolean) Boolean
  - linkTrees(BinomialNode~T~, BinomialNode~T~) Unit
  + clearTrace() Unit
  + traceTo(Collection~TracePoint~) BinomialQueue~T~
  + min() T?
   Boolean empty
   Int size
}
class Brockian~P~ {
<<Interface>>
  + hitWithoutVisibleReason(P, Hittable) Unit
}
class BrockianUltraCricket {
  + BrockianUltraCricket() 
  + chooseTarget(Collection~Human~) Human
  + hitWithoutVisibleReason(Mice, Hittable) Unit
  + result() Unit
  - performHit(Mice, Hittable) Unit
  + addPlayer(Mice) Boolean
  + start() Unit
  + playRound(Mice, Human) Unit
  + runAway() Unit
  - recordRound(Mice, Human) Unit
  + bat(Mice, Human) Unit
  + resultByPlayer(Mice) Unit
  - requireActiveGame() Unit
  + removePlayer(Mice) Boolean
  + finish() Unit
   Boolean finished
   Boolean started
   Int roundsPlayed
   MatchResult? lastResult
   PlayerResult? lastPlayerResult
}
class Creature {
  + Creature(String) 
  + manifestPhysically() Unit
   String name
   Boolean physicallyManifested
}
class Cricket~P, T~ {
  + Cricket(() -~ Collection~P~) 
  + chooseTarget(Collection~T~) T
}
class Game~T~ {
  + Game(() -~ Collection~T~) 
  # hasPlayer(T) Boolean
  + finish() Unit
  + addPlayer(T) Boolean
  # snapshotPlayers() List~T~
  + resultByPlayer(T) Unit
  + result() Unit
  + removePlayer(T) Boolean
  # requeuePlayer(T, (T) -~ Unit) Unit
  # highestScoringPlayer() T?
  + start() Unit
   Collection~T~ players
}
class Hittable {
<<Interface>>
  + receiveUnexpectedHit() Unit
}
class Human {
  + Human(String) 
  + receiveUnexpectedHit() Unit
  + runAwayFromDanger() Unit
   Int unexpectedHitCount
   Boolean runningAway
}
class Iterator {
  + Iterator() 
  + remove() Unit
  + hasNext() Boolean
  + next() T
}
class MainKt {
  + main() Unit
}
class Mice {
  + Mice(String, Int) 
  + playBrockianUltraCricket(Hittable) Unit
  + compareTo(Mice) Int
  + awardPoint(Int) Unit
  + argueAboutMeaningOfLife() Unit
  + resolveAllQuestionsOnceAndForAll() Unit
   Boolean hasResolvedAllQuestions
   Int meaningOfLifeArguments
   Int score
   Int successfulHits
}
class MicePlayer {
  + MicePlayer(String, Int) 
  + enterMatch() Unit
  + leaveMatch() Unit
   Int enteredMatches
   Boolean inMatch
}
class Playable {
<<Interface>>
   Int score
}
class TracePoint {
<<enumeration>>
  - TracePoint() 
  + values() TracePoint[]
  + valueOf(String) TracePoint
   EnumEntries~TracePoint~ entries
}
class UltraGame {
<<Interface>>
  + runAway() Unit
}
class data  MatchResult {
  + MatchResult(Mice?, Int, Int, Boolean) 
   Mice? leader
   Boolean finished
   Int roundsPlayed
   Int escapedTargets
}
class data  PlayerResult {
  + PlayerResult(Mice, Int, Int, Boolean) 
   Boolean leader
   Mice player
   Int score
   Int successfulHits
}
class data  Round {
  + Round(Mice, Human, Int) 
   Mice player
   Human target
   Int scoreAfterHit
}

BattingGame~P, T~  ..>  Hittable 
BattingGame~P, T~  ..>  Playable 
BinomialQueue~T~  -->  BinomialNode~T~ 
BinomialQueue~T~  ..>  BinomialNode~T~ : «create»
BinomialQueue~T~ "1" *--> "head 1" BinomialNode~T~ 
BinomialQueue~T~  ..>  Iterator : «create»
Brockian~P~  ..>  Playable 
BrockianUltraCricket  ..>  BinomialQueue~T~ : «create»
BrockianUltraCricket  ..>  Brockian~P~ 
BrockianUltraCricket  -->  Cricket~P, T~ 
BrockianUltraCricket  ..>  Cricket~P, T~ : «create»
BrockianUltraCricket "1" *--> "escapedTargets *" Human 
BrockianUltraCricket  ..>  UltraGame 
BrockianUltraCricket  ..>  data  MatchResult : «create»
BrockianUltraCricket "1" *--> "computedResult 1" data  MatchResult 
BrockianUltraCricket "1" *--> "computedPlayerResult 1" data  PlayerResult 
BrockianUltraCricket  ..>  data  PlayerResult : «create»
BrockianUltraCricket  ..>  data  Round : «create»
BrockianUltraCricket "1" *--> "rounds *" data  Round 
Cricket~P, T~  ..>  BattingGame~P, T~ 
Cricket~P, T~  -->  Game~T~ 
Cricket~P, T~  ..>  Game~T~ : «create»
Cricket~P, T~  ..>  Hittable 
Cricket~P, T~  ..>  Playable 
Game~T~  ..>  Playable 
Human  -->  Creature 
Human  ..>  Creature : «create»
Human  ..>  Hittable 
BinomialQueue~T~  -->  Iterator 
Mice  -->  Creature 
Mice  ..>  Creature : «create»
Mice  ..>  Playable 
MicePlayer  -->  Mice 
MicePlayer  ..>  Mice : «create»
MicePlayer  ..>  Playable 
BinomialQueue~T~  -->  TracePoint 
BrockianUltraCricket  -->  data  MatchResult 
data  MatchResult "1" *--> "leader 1" Mice 
BrockianUltraCricket  -->  data  PlayerResult 
data  PlayerResult "1" *--> "player 1" Mice 
BrockianUltraCricket  -->  data  Round 
data  Round "1" *--> "target 1" Human 
data  Round "1" *--> "player 1" Mice 

```

### Интерфейсы

```kotlin
interface Hittable {
    fun receiveUnexpectedHit()
}

interface Playable {
    val score: Int
}
```

### Классы DTO

| Класс        | Описание                                                      |
|--------------|---------------------------------------------------------------|
| `Creature`   | Абстрактный базовый класс для всех существ                    |
| `Human`      | Человек, может получить неожиданный удар и убежать            |
| `Mice`       | Мышь с очками, умеет играть в крикет и спорить о смысле жизни |
| `MicePlayer` | Мышь-игрок, участвующая в матче                               |

### Игровая логика

`BrockianUltraCricket` (`src/main/kotlin/game/BrockianUltraCricket.kt`):

- Наследует `Cricket` с биномиальной кучей для хранения игроков
- Поддерживает раунды, подсчет очков, выбор целей
- Выбор цели: предпочитает не убегающих людей с наименьшим количеством полученных ударов

### Тестовое покрытие

Файл `src/test/kotlin/DomainTest.kt`:

| Тест                                                         | Проверяемый класс    | Описание                    |
|--------------------------------------------------------------|----------------------|-----------------------------|
| `creatureCanManifestPhysicallyWithoutLosingIdentity`         | Creature             | Манифестация существа       |
| `humanCanReceiveUnexpectedHitAndRunAwayFromDanger`           | Human                | Получение удара и побег     |
| `miceCanHandleExistentialRoutine`                            | Mice                 | Споры о смысле жизни        |
| `miceCanPlayBrockianUltraCricketByHittingATarget`            | Mice                 | Игра в крикет               |
| `miceAreOrderedByScore`                                      | Mice                 | Сравнение по очкам          |
| `micePlayerCanEnterMatch`                                    | MicePlayer           | Вход/выход из матча         |
| `brockianUltraCricketCanHandleBasicLifecycle`                | BrockianUltraCricket | Жизненный цикл игры         |
| `brockianUltraCricketBatIncrementsPlayerScore`               | BrockianUltraCricket | Увеличение очков при ударе  |
| `brockianUltraCricketCanHitWithoutVisibleReason`             | BrockianUltraCricket | Удар без видимой причины    |
| `brockianUltraCricketChoosesBestAvailableHumanTarget`        | BrockianUltraCricket | Выбор цели                  |
| `gameStoresPlayersInternallyThroughAddPlayerAndRemovePlayer` | Cricket              | Добавление/удаление игроков |
| `brockianUltraCricketCanPlayARoundForPlayerAndTarget`        | BrockianUltraCricket | Полный раунд игры           |

---

## Структура проекта

```
src/
├── main/kotlin/
│   ├── BinomialQueue.kt           # Биномиальная куча
│   ├── Main.kt                    # Точка входа
│   ├── dto/                       # DTO классы
│   │   ├── Creature.kt
│   │   ├── Human.kt
│   │   ├── Mice.kt
│   │   ├── MicePlayer.kt
│   │   └── Playable.kt
│   ├── game/                      # Игровая логика
│   │   ├── BattingGame.kt
│   │   ├── Brockian.kt
│   │   ├── BrockianUltraCricket.kt
│   │   ├── Cricket.kt
│   │   ├── Game.kt
│   │   ├── Hittable.kt
│   │   └── UltraGame.kt
│   └── mymath/
│       └── Arctg.kt               # Функция arctg
└── test/kotlin/
    ├── BinomialQueueTest.kt      # Тесты биномиальной кучи
    ├── DomainTest.kt             # Тесты доменной модели
    └── MathTest.kt               # Тесты arctg
```

---

## Запуск тестов

```bash
./gradlew test
```

---

## Выводы

1. **Функция arctg**: Реализована с использованием оптимизаций для ускорения сходимости ряда. Тесты покрывают граничные случаи, свойство нечетности и монотонность.

2. **Биномиальная куча**: Реализован полный функционал с трассировкой характерных точек. Тесты проверяют как функциональность, так и последовательность выполнения операций.

3. **Доменная модель**: Построена объектная модель по описанию предметной области с иерархией существ и игровой логикой. Тесты покрывают все аспекты поведения классов.
