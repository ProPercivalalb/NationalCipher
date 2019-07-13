package nationalcipher.cipher.base;

public abstract class VigenereType {

	public static final VigenereType NONE = new None();
	public static final VigenereType BEAUFORT = new Beaufort();
	public static final VigenereType PORTA = new Porta();
	public static final VigenereType PORTA_VARIANT = new PortaVariant();
	public static final VigenereType VARIANT = new Variant();
	public static final VigenereType VIGENERE = new Vigenere();
	public static final VigenereType[] SLIDEFAIR_LIST = new VigenereType[] {BEAUFORT, VARIANT, VIGENERE};
	public static final VigenereType[] NORMAL_LIST = new VigenereType[] {BEAUFORT, PORTA, VARIANT, VIGENERE};
	
	private VigenereType() {} //Can't create any more instances
	
	private static class None extends VigenereType {
		@Override
		public char encode(char textCharId, char keyCharId) {
            return textCharId;
		}
		
		@Override
		public char decode(char textCharId, char keyCharId) {
            return textCharId;
		}
	}
	
	private static class Beaufort extends VigenereType {
		@Override
		public char encode(char textCharId, char keyCharId) {
			return (char)((keyCharId - textCharId + 26) % 26 + 'A');
		}
		
		@Override
		public char decode(char textCharId, char keyCharId) {
			return encode(textCharId, keyCharId);
		}
	}
	
	private static class Porta extends VigenereType {
		@Override
		public char encode(char textCharId, char keyCharId) {
			char key = (char)((keyCharId - 'A') / 2);
            char newCharId = textCharId;
            if(newCharId < 13 + 'A') {
            	newCharId += key;
                if(newCharId < 13 + 'A')
                	newCharId += 13;
            }
            else {
            	newCharId -= key;
                if(newCharId > 12 + 'A')
                	newCharId -= 13;
            }
            return newCharId;
		}
		
		@Override
		public char decode(char textCharId, char keyCharId) {
            return encode(textCharId, keyCharId);
		}
	}
	
	private static class PortaVariant extends VigenereType {
		@Override
		public char encode(char textCharId, char keyCharId) {
			char key = (char)((keyCharId - 'A') / 2);
            char newCharId = textCharId;
            if(newCharId < 13 + 'A') {
            	newCharId += 13;
            	newCharId -= key;
                if(newCharId < 13 + 'A')
                	newCharId += 13;
            }
            else {
            	newCharId += key;
                if(newCharId > 12 + 'A')
                	newCharId -= 13;
            }
            return newCharId;
		}
		
		@Override
		public char decode(char textCharId, char keyCharId) {
            return encode(textCharId, keyCharId);
		}
	}
	
	private static class Variant extends VigenereType {
		@Override
		public char encode(char textCharId, char keyCharId) { //'A' - 'A' = 0   mod 26 = 0
			return (char)((-keyCharId + textCharId + 26) % 26 + 'A');
		}
		
		@Override
		public char decode(char textCharId, char keyCharId) { //'A' + 'A' = 130 mod 26 = 0
			return (char)((keyCharId + textCharId) % 26 + 'A');
		}
	}
	
	private static class Vigenere extends VigenereType {
		@Override
		public char encode(char textCharId, char keyCharId) { //'A' + 'A' = 130 mod 26 = 0
			return (char)((keyCharId + textCharId) % 26 + 'A');
		}
		
		@Override
		public char decode(char textCharId, char keyCharId) { //'A' - 'A' = 0   mod 26 = 0
			return (char)((-keyCharId + textCharId + 26) % 26 + 'A');
		}
	}
	
	public abstract char encode(char textCharId, char keyCharId);
	
	public abstract char decode(char textCharId, char keyCharId);
}
