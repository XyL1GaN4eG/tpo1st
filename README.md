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

```plantuml
@startuml

!theme plain
left to right direction
skinparam linetype ortho

interface BattingGame<P, T> << interface >> {
  + bat(P, T): Unit
}
class BinomialNode<T> {
  + BinomialNode(T, Int, BinomialNode<T>?, BinomialNode<T>?, BinomialNode<T>?): 
   parent: BinomialNode<T>?
   sibling: BinomialNode<T>?
   degree: Int
   key: T
   child: BinomialNode<T>?
}
class BinomialQueue<T> {
  + BinomialQueue(): 
  + contains(T): Boolean
  + addAll(Collection<T>): Boolean
  + extractMin(): T?
  + iterator(): Iterator<T>
  + retainAll(Collection<T>): Boolean
  - trace(TracePoint): Unit
  + remove(T): Boolean
  + clear(): Unit
  - mergeRootLists(BinomialNode<T>?): BinomialNode<T>?
  + add(T): Boolean
  + union(BinomialQueue<T>): Unit
  + removeAll(Collection<T>): Boolean
  + containsAll(Collection<T>): Boolean
  - binomialTreeSize(Int): Int
  - rebuildKeeping((T) -> Boolean): Boolean
  - linkTrees(BinomialNode<T>, BinomialNode<T>): Unit
  + clearTrace(): Unit
  + traceTo(Collection<TracePoint>): BinomialQueue<T>
  + min(): T?
   empty: Boolean
   size: Int
}
interface Brockian<P> << interface >> {
  + hitWithoutVisibleReason(P, Hittable): Unit
}
class BrockianUltraCricket {
  + BrockianUltraCricket(): 
  + chooseTarget(Collection<Human>): Human
  + hitWithoutVisibleReason(Mice, Hittable): Unit
  + result(): Unit
  - performHit(Mice, Hittable): Unit
  + addPlayer(Mice): Boolean
  + start(): Unit
  + playRound(Mice, Human): Unit
  + runAway(): Unit
  - recordRound(Mice, Human): Unit
  + bat(Mice, Human): Unit
  + resultByPlayer(Mice): Unit
  - requireActiveGame(): Unit
  + removePlayer(Mice): Boolean
  + finish(): Unit
   finished: Boolean
   started: Boolean
   roundsPlayed: Int
   lastResult: MatchResult?
   lastPlayerResult: PlayerResult?
}
class Creature {
  + Creature(String): 
  + manifestPhysically(): Unit
   name: String
   physicallyManifested: Boolean
}
class Cricket<P, T> {
  + Cricket(() -> Collection<P>): 
  + chooseTarget(Collection<T>): T
}
class Game<T> {
  + Game(() -> Collection<T>): 
  # hasPlayer(T): Boolean
  + finish(): Unit
  + addPlayer(T): Boolean
  # snapshotPlayers(): List<T>
  + resultByPlayer(T): Unit
  + result(): Unit
  + removePlayer(T): Boolean
  # requeuePlayer(T, (T) -> Unit): Unit
  # highestScoringPlayer(): T?
  + start(): Unit
   players: Collection<T>
}
interface Hittable << interface >> {
  + receiveUnexpectedHit(): Unit
}
class Human {
  + Human(String): 
  + receiveUnexpectedHit(): Unit
  + runAwayFromDanger(): Unit
   unexpectedHitCount: Int
   runningAway: Boolean
}
class Iterator {
  + Iterator(): 
  + remove(): Unit
  + hasNext(): Boolean
  + next(): T
}
class MainKt {
  + main(): Unit
}
class Mice {
  + Mice(String, Int): 
  + playBrockianUltraCricket(Hittable): Unit
  + compareTo(Mice): Int
  + awardPoint(Int): Unit
  + argueAboutMeaningOfLife(): Unit
  + resolveAllQuestionsOnceAndForAll(): Unit
   hasResolvedAllQuestions: Boolean
   meaningOfLifeArguments: Int
   score: Int
   successfulHits: Int
}
class MicePlayer {
  + MicePlayer(String, Int): 
  + enterMatch(): Unit
  + leaveMatch(): Unit
   enteredMatches: Int
   inMatch: Boolean
}
interface Playable << interface >> {
   score: Int
}
enum TracePoint << enumeration >> {
  - TracePoint(): 
  + values(): TracePoint[]
  + valueOf(String): TracePoint
   entries: EnumEntries<TracePoint>
}
interface UltraGame << interface >> {
  + runAway(): Unit
}
entity data  MatchResult << data >> {
  + MatchResult(Mice?, Int, Int, Boolean): 
   leader: Mice?
   finished: Boolean
   roundsPlayed: Int
   escapedTargets: Int
}
entity data  PlayerResult << data >> {
  + PlayerResult(Mice, Int, Int, Boolean): 
   leader: Boolean
   player: Mice
   score: Int
   successfulHits: Int
}
entity data  Round << data >> {
  + Round(Mice, Human, Int): 
   player: Mice
   target: Human
   scoreAfterHit: Int
}

BattingGame           -[#595959,dashed]->  Hittable             
BattingGame           -[#595959,dashed]->  Playable             
BinomialNode          +-[#820000,plain]-  BinomialQueue        
BinomialQueue         -[#595959,dashed]->  BinomialNode         : "«create»"
BinomialQueue        "1" *-[#595959,plain]-> "head\n1" BinomialNode         
BinomialQueue         -[#595959,dashed]->  Iterator             : "«create»"
Brockian              -[#595959,dashed]->  Playable             
BrockianUltraCricket  -[#595959,dashed]->  BinomialQueue        : "«create»"
BrockianUltraCricket  -[#008200,dashed]-^  Brockian             
BrockianUltraCricket  -[#000082,plain]-^  Cricket              
BrockianUltraCricket  -[#595959,dashed]->  Cricket              : "«create»"
BrockianUltraCricket "1" *-[#595959,plain]-> "escapedTargets\n*" Human                
BrockianUltraCricket  -[#008200,dashed]-^  UltraGame            
BrockianUltraCricket  -[#595959,dashed]->  data  MatchResult    : "«create»"
BrockianUltraCricket "1" *-[#595959,plain]-> "computedResult\n1" data  MatchResult    
BrockianUltraCricket "1" *-[#595959,plain]-> "computedPlayerResult\n1" data  PlayerResult   
BrockianUltraCricket  -[#595959,dashed]->  data  PlayerResult   : "«create»"
BrockianUltraCricket  -[#595959,dashed]->  data  Round          : "«create»"
BrockianUltraCricket "1" *-[#595959,plain]-> "rounds\n*" data  Round          
Cricket               -[#008200,dashed]-^  BattingGame          
Cricket               -[#000082,plain]-^  Game                 
Cricket               -[#595959,dashed]->  Game                 : "«create»"
Cricket               -[#595959,dashed]->  Hittable             
Cricket               -[#595959,dashed]->  Playable             
Game                  -[#595959,dashed]->  Playable             
Human                 -[#000082,plain]-^  Creature             
Human                 -[#595959,dashed]->  Creature             : "«create»"
Human                 -[#008200,dashed]-^  Hittable             
Iterator              +-[#820000,plain]-  BinomialQueue        
Mice                  -[#000082,plain]-^  Creature             
Mice                  -[#595959,dashed]->  Creature             : "«create»"
Mice                  -[#008200,dashed]-^  Playable             
MicePlayer            -[#000082,plain]-^  Mice                 
MicePlayer            -[#595959,dashed]->  Mice                 : "«create»"
MicePlayer            -[#008200,dashed]-^  Playable             
TracePoint            +-[#820000,plain]-  BinomialQueue        
data  MatchResult     +-[#820000,plain]-  BrockianUltraCricket 
data  MatchResult    "1" *-[#595959,plain]-> "leader\n1" Mice                 
data  PlayerResult    +-[#820000,plain]-  BrockianUltraCricket 
data  PlayerResult   "1" *-[#595959,plain]-> "player\n1" Mice                 
data  Round           +-[#820000,plain]-  BrockianUltraCricket 
data  Round          "1" *-[#595959,plain]-> "target\n1" Human                
data  Round          "1" *-[#595959,plain]-> "player\n1" Mice                 
@enduml
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
