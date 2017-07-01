package com.aegeus.game;

public enum Legion {
	FEROCIOUS(0, "Ferocious"),
	NIMBLE(1, "Nimble") {
		private int stack;

		public boolean stack() {
			stack++;
			return stack >= 4;
		}

		public void clearStacks() {
			stack = 0;
		}
	},
	CRYPTIC(2, "Cryptic"),
	DIVINE(3, "Divine") {
		private int stack;

		public boolean stack() {
			stack++;
			return stack >= 4;
		}

		public void clearStacks() {
			stack = 0;
		}
	};

	private final int id;
	private final String name;

	Legion(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Legion fromID(int id) {
		for (Legion l : values())
			if (l.id == id) return l;
		return null;
	}

	public Legion fromName(String name) {
		for (Legion l : values())
			if (l.name.equalsIgnoreCase(name)) return l;
		return null;
	}
}