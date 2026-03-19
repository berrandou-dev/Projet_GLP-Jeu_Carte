package engine.data;

public enum CombinationType {
	SIMPLE,
	DOUBLE,
	SERIES,
	BOMB,
	DOUBLE_JOKER, 
	INVALID;
    
	@Override
 	public String toString() {
		switch(this) {
			case SIMPLE:
				return "Simple";
			case DOUBLE:
				return "Double";
			case SERIES:
				return "Series";
			case BOMB:
				return "Bomb";
			case DOUBLE_JOKER:
				return "Double Joker";
			case INVALID:
				return "Invalid";
			default:
				return name();
		}
	}
	
	public boolean isValid() {
		return this != INVALID;
	}
    

	public int getStrength() {
		switch(this) {
			case SIMPLE:
				return 1;
			case DOUBLE:
				return 2;
			case SERIES:
				return 3;
			case BOMB:
				return 4;
			case DOUBLE_JOKER:
				return 5;
			case INVALID:
				return 0;
			default:
				return 0;
		}
	}

	public boolean canBeat(CombinationType other) {
		if (this == DOUBLE_JOKER) return true;
		if (this == BOMB && other != BOMB) return true;
		return this.getStrength() > other.getStrength();
	}
}
