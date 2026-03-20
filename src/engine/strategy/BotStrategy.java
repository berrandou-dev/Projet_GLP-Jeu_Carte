package engine.strategy;

import java.util.List;
import engine.data.Card;
import engine.process.Combination;

public interface BotStrategy {
	Combination chooseCombination(List<Card> hand, Combination lastCombination);
    boolean shouldPass(List<Card> hand, Combination lastCombination);
}
