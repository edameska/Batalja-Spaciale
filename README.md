# 🚀 Batalja Spaciale 🚀

### Batalja Spaciale Strategies

🪐 Welcome, Galactic Strategists! Dive into the universe of Batalja Spaciale, a multiplayer space conquest game where cunning strategies determine the fate of entire galaxies.

### What to Expect

📏📍 **Eucledean Distance Strategy** also known as __Correctest Distance__ or Player.java in this repository, utilizes the Euclidean distance metric to attack enemies.<br>
⏳📐 **WaitOrDie Strategy** or Player2.java within this repository, entails waiting for the optimal conditions to attack enemy planets, while still attacking the closest enemy.<br>
📍⏳  **WoundAndWait Strategy** or Player3.java, is a combination of the aforementioned strategies, however, in the beginning it attacks neutral planets using the Eucledean distance metric, 
and then, when all planets are conquered by either team, it uses the __WaitOrDie__ strategy.


### Q-Learning Bot

🚀 **Q-Learning Bot**: We decided to utilize the Q-Learning algorithm, a reinforcement learning approach. This bot continuously learns and refines its decision-making based on rewards from its actions. The Q-Learning Bot is located in the `Better-version` folder of this repository.

#### Key Concepts:

- **Q-table**: A Map that stores the expected utility (Q-value) of taking each action in each state.
- **LEARNING_RATE**: Determines how much new information overrides the old.
- **DISCOUNT_FACTOR**: Importance of future rewards.
- **EXPLORATION_RATE**: How often to choose a random action (exploration).
- **EXPLORATION_DECAY**: Rate at which exploration decreases over time.
- **MIN_EXPLORATION_RATE**: Minimum value for the exploration rate.

### Galactic Visionaries
⭐[Elena Dameska](https://github.com/edameska)<br>
🌟[Sofija Kochovska](https://github.com/sof42)
