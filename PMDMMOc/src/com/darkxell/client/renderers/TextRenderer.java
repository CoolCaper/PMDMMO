package com.darkxell.client.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Message;

public class TextRenderer {

	public static enum PMDChar {
		new_line("<br>", 0, 0),
		tabulation("<tab>", 0, 0),
		space(" ", 0, 4),
		space_number("<spn>", 0, 6),
		a("a", 1, 5),
		b("b", 2, 5),
		c("c", 3, 5),
		d("d", 4, 5),
		e("e", 5, 5),
		f("f", 6, 5),
		g("g", 7, 5),
		h("h", 8, 5),
		i("i", 9, 3),
		j("j", 10, 3),
		k("k", 11, 5),
		l("l", 12, 3),
		m("m", 13, 8),
		n("n", 14, 5),
		o("o", 15, 5),
		p("p", 16, 5),
		q("q", 17, 5),
		r("r", 18, 5),
		s("s", 19, 5),
		t("t", 20, 4),
		u("u", 21, 5),
		v("v", 22, 6),
		w("w", 23, 8),
		x("x", 24, 6),
		y("y", 25, 5),
		z("z", 26, 5),
		A("A", 27, 6),
		B("B", 28, 6),
		C("C", 29, 6),
		D("D", 30, 6),
		E("E", 31, 5),
		F("F", 32, 5),
		G("G", 33, 6),
		H("H", 34, 6),
		I("I", 35, 4),
		J("J", 36, 6),
		K("K", 37, 6),
		L("L", 38, 5),
		M("M", 39, 8),
		N("N", 40, 6),
		O("O", 41, 6),
		P("P", 42, 6),
		Q("Q", 43, 6),
		R("R", 44, 6),
		S("S", 45, 6),
		T("T", 46, 6),
		U("U", 47, 6),
		V("V", 48, 6),
		W("W", 49, 10),
		X("X", 50, 6),
		Y("Y", 51, 6),
		Z("Z", 52, 6),
		num1("1", 53, 6),
		num2("2", 54, 6),
		num3("3", 55, 6),
		num4("4", 56, 6),
		num5("5", 57, 6),
		num6("6", 58, 6),
		num7("7", 59, 6),
		num8("8", 60, 6),
		num9("9", 61, 6),
		num0("0", 62, 6),
		column(":", 63, 2),
		plus("+", 64, 6),
		minus("-", 65, 5),
		coma(",", 66, 3),
		dot(".", 67, 3),
		exclamation_("<!>", 68, 4),
		exclamation("!", 69, 4),
		interrogation_("<?>", 70, 6),
		interrogation("?", 71, 6),
		apostrophe_("`", 72, 3),
		apostrophe("'", 73, 3),
		quote_("<\">", 74, 6),
		quote("\"", 75, 6),
		male("<male>", 76, 8),
		female("<female>", 77, 6),
		space_visible("_", 78, 10),
		three_dots("<dots>", 79, 9),
		parenthesis_o("(", 80, 5),
		parenthesis_c(")", 81, 5),
		slash("/", 82, 5),
		poke1("<poke1>", 83, 8),
		poke2("<poke2>", 84, 7),
		e_accent("�", 85, 7),
		tilde("~", 86, 6),
		sharp("#", 87, 9),
		music("<music>", 88, 8),
		arrow_up("<arrow-up>", 89, 8),
		arrow_right("<arrow-right>", 90, 8),
		arrow_down("<arrow-down>", 91, 8),
		arrow_left("<arrow-left>", 92, 8),
		tick("<tick>", 94, 9),
		star("<star>", 95, 7),
		glued("<glued>", 96, 8),
		mission("<mission>", 97, 8),
		mission_accepted("<mission-a>", 98, 10),
		news("<news>", 99, 10),
		story("<story>", 100, 9),
		speech_bubble("<speech>", 101, 9),
		key_A("<key-a>", 102, 10),
		key_B("<key-b>", 103, 10),
		key_L("<key-l>", 104, 10),
		key_R("<key-r>", 105, 10),
		key_PLUS("<key-+>", 106, 10),
		key_SELECT1("<select1>", 107, 10),
		key_SELECT2("<select2>", 108, 10),
		buy_1("<1>", 109, 6),
		buy_2("<2>", 110, 6),
		buy_3("<3>", 111, 6),
		buy_4("<4>", 112, 6),
		buy_5("<5>", 113, 6),
		buy_6("<6>", 114, 6),
		buy_7("<7>", 115, 6),
		buy_8("<8>", 116, 6),
		buy_9("<9>", 117, 6),
		buy_0("<0>", 118, 6),
		orb("<orb>", 119, 10),
		tm_used("<tmu>", 120, 10),
		hm("<hm>", 121, 10),
		tm_0("<tm0>", 122, 10),
		tm_1("<tm1>", 123, 10),
		tm_2("<tm2>", 124, 10),
		tm_3("<tm3>", 125, 10),
		tm_4("<tm4>", 126, 10),
		tm_5("<tm5>", 127, 10),
		tm_6("<tm6>", 128, 10),
		tm_7("<tm7>", 129, 10),
		tm_8("<tm8>", 130, 10),
		tm_9("<tm9>", 131, 10),
		tm_10("<tm10>", 132, 10),
		tm_11("<tm11>", 133, 10),
		tm_12("<tm12>", 134, 10),
		tm_13("<tm13>", 135, 10),
		tm_14("<tm14>", 136, 10),
		tm_15("<tm15>", 137, 10),
		tm_16("<tm16>", 138, 10),
		tm_17("<tm17>", 139, 10),
		type_0("<type-0>", 140, 11),
		type_1("<type-1>", 141, 11),
		type_2("<type-2>", 142, 11),
		type_3("<type-3>", 143, 11),
		type_4("<type-4>", 144, 11),
		type_5("<type-5>", 145, 11),
		type_6("<type-6>", 146, 11),
		type_7("<type-7>", 147, 11),
		type_8("<type-8>", 148, 11),
		type_9("<type-9>", 149, 11),
		type_10("<type-10>", 150, 11),
		type_11("<type-11>", 151, 11),
		type_12("<type-12>", 152, 11),
		type_13("<type-13>", 153, 11),
		type_14("<type-14>", 154, 11),
		type_15("<type-15>", 155, 11),
		type_16("<type-16>", 156, 11),
		type_17("<type-17>", 157, 11),
		minusd(null, 160, 7),
		num1d(null, 161, 7),
		num2d(null, 162, 7),
		num3d(null, 163, 7),
		num4d(null, 164, 7),
		num5d(null, 165, 7),
		num6d(null, 166, 7),
		num7d(null, 167, 7),
		num8d(null, 168, 7),
		num9d(null, 169, 7),
		num0d(null, 170, 7);

		public static PMDChar find(String value) {
			for (PMDChar c : values())
				if (c.value != null && c.value.equals(value))
					return c;
			return null;
		}

		/** Location on the Font sprite. */
		public final int id;
		public final String value;

		/** Width of the sprite. */
		public final int width;

		private PMDChar(String value, int id, int width) {
			this.value = value;
			this.id = id;
			this.width = width;
		}

	}

	public static final int CHAR_HEIGHT = 11;
	private static final int GRID_COLS = 20;
	private static final int GRID_WIDTH = CHAR_HEIGHT, GRID_HEIGHT = CHAR_HEIGHT;
	public static final TextRenderer instance = new TextRenderer();
	public static final int LINE_SPACING = 3;
	public static final int TAB_ALIGN = 25;

	/** Called on startup to load the font. */
	public static void load() {
	}

	private HashMap<PMDChar, PMDChar> dungeonChars;
	private HashMap<PMDChar, BufferedImage> sprites;

	private TextRenderer() {
		this.sprites = new HashMap<PMDChar, BufferedImage>();
		this.dungeonChars = new HashMap<PMDChar, PMDChar>();
		BufferedImage source = Res.getBase("resources/hud/font.png");
		for (PMDChar c : PMDChar.values())
			this.sprites.put(c, Res.createimage(source, c.id % GRID_COLS * GRID_WIDTH,
					(c.id - c.id % GRID_COLS) / GRID_COLS * GRID_HEIGHT, GRID_WIDTH, GRID_HEIGHT));

		this.dungeonChars.put(PMDChar.minus, PMDChar.minusd);
		this.dungeonChars.put(PMDChar.num1, PMDChar.num1d);
		this.dungeonChars.put(PMDChar.num2, PMDChar.num2d);
		this.dungeonChars.put(PMDChar.num3, PMDChar.num3d);
		this.dungeonChars.put(PMDChar.num4, PMDChar.num4d);
		this.dungeonChars.put(PMDChar.num5, PMDChar.num5d);
		this.dungeonChars.put(PMDChar.num6, PMDChar.num6d);
		this.dungeonChars.put(PMDChar.num7, PMDChar.num7d);
		this.dungeonChars.put(PMDChar.num8, PMDChar.num8d);
		this.dungeonChars.put(PMDChar.num9, PMDChar.num9d);
		this.dungeonChars.put(PMDChar.num0, PMDChar.num0d);
	}

	/**
	 * Adds spaces in front of the input number to match the input number of
	 * digits.
	 */
	public Message alignNumber(int n, int digits) {
		String s = Integer.toString(n);
		int diff = s.length() - digits;
		for (int i = diff; i < 0; ++i)
			s = PMDChar.space_number.value + s;
		return new Message(s, false);
	}

	public ArrayList<PMDChar> decode(String text) {
		ArrayList<PMDChar> chars = new ArrayList<TextRenderer.PMDChar>();
		if (text == null)
			return chars;
		int c = 0;
		String value;
		while (c < text.length()) {
			if (text.charAt(c) == '<') {
				value = "";
				++c;
				while (c < text.length() && text.charAt(c) != '>') {
					value += text.charAt(c);
					++c;
				}
				chars.add(PMDChar.find("<" + value + ">"));
			} else
				chars.add(PMDChar.find(text.substring(c, c + 1)));
			++c;
		}

		for (c = 0; c < chars.size(); ++c)
			if (chars.get(c) == null)
				chars.remove(c);
		return chars;
	}

	/** Renders the input message at the topright x, y coordinates. */
	public void render(Graphics2D g, Message message, int x, int y) {
		this.render(g, message.toString(), x, y);
	}

	public void render(Graphics2D g, String text, int x, int y)
	{
		this.render(g, text, x, y, false);
	}

	/** Renders the input text at the topright x, y coordinates. */
	public void render(Graphics2D g, String text, int x, int y, boolean dungeonHUD) {
		ArrayList<PMDChar> chars = this.decode(text);
		ArrayList<PMDChar> toprint = new ArrayList<TextRenderer.PMDChar>();
		if (dungeonHUD) for (PMDChar c : chars)
			if (this.dungeonChars.containsKey(c)) toprint.add(this.dungeonChars.get(c));
			else toprint.add(c);
		else toprint.addAll(chars);
		this.render(g, toprint, x, y, dungeonHUD);
	}

	/** Renders the input text at the topright x, y coordinates. */
	public void render(Graphics2D g, List<PMDChar> text, int x, int y, boolean dungeonHUD) {
		int w = 0;
		for (PMDChar c : text) {
			g.drawImage(this.sprites.get(c), x + w, y, null);
			if (c == PMDChar.tabulation)
				w += this.tabWidth(w);
			else
				w += c.width;
		}
	}

	/** Transforms a String into an array of strings printable to the screen. */
	public ArrayList<String> splitLines(String text, int boxwidth) {
		ArrayList<String> textlines = new ArrayList<>();
		if (text == null)
			return textlines;
		int currentlength = 0, iterator = 0;
		String[] parts = text.split("\n");
		for (int i = 0; i < parts.length; i++) {
			textlines.add("");
			currentlength = 0;
			String[] words = parts[i].split(" ");
			for (int j = 0; j < words.length; j++)
				if (!words[j].startsWith(PMDChar.new_line.value)
						&& (currentlength == 0 || currentlength + this.width(words[j]) < boxwidth)) {
					textlines.set(iterator, textlines.get(iterator) + words[j] + " ");
					currentlength += this.width(words[j] + " ");
				} else {
					textlines.add(words[j] + " ");
					++iterator;
					currentlength = this.width(words[j] + " ");
				}
			++iterator;
		}
		return textlines;
	}

	/**
	 * @return The width of a Tabulation character, depending on the current
	 *         width of the text.
	 */
	public int tabWidth(int currentWidth) {
		return TAB_ALIGN - currentWidth % TAB_ALIGN;
	}

	/** @return The width of the input message. */
	public int width(Message message) {
		if (message == null)
			return 0;
		return this.width(message.toString());
	}

	/** @return The width of the input text. */
	public int width(String text) {
		if (text == null)
			return 0;
		return this.width(this.decode(text));
	}

	public int width(List<PMDChar> chars) {
		int w = 0;
		for (PMDChar c : chars)
			if (c == PMDChar.tabulation) w += this.tabWidth(w);
			else w += c.width;
		return w;
	}

}