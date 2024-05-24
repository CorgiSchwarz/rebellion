# Intro
Java implementation of Netlogo Rebellion model.
It generates the result in `result/agent.csv`
# Sample Input
```
--length 40 --width 40 --copDensity 0.04 --agentDensity 0.7 --vision 7.0 --governmentLegitimacy 0.82 --maxJailTerm 30 --movement true --ticks 500
```

```
--length 40 --width 40 --copDensity 0.04 --agentDensity 0.7 --vision 7.0 --governmentLegitimacy 0.82 --maxJailTerm 30 --movement true --extension true --ticks 500
```
# Parameters
| Parameter              | Type    | Default | Description                                  |
| ---------------------- | ------- | ------- |----------------------------------------------|
| --length               | Integer | 40      | The length for the map.                      |
| --width                | Integer | 40      | The width for the map.                       |
| --copDensity           | Double  | 0.04    | The density of cops.                         |
| --agentDensity         | Double  | 0.7     | The density of agents.                       |
| --vision               | Double  | 7.0     | The vision radius.                           |
| --governmentLegitimacy | Double  | 0.82    | The legitimacy of the government.            |
| --maxJailTerm          | Integer | 30      | The maximum ticks an agent is put into jail. |
| --movement             | Boolean | True    | Whether an agent moves.                      |
| --ticks                | Integer | 500     | The ticks you intend to run.                 |
| --extension            | Boolean | False   | Whether to enable the extension.             |


