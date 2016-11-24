package nationalcipher.cipher.base;

public abstract class VigenereType {

	public static final VigenereType BEAUFORT = new Beaufort();
	public static final VigenereType PORTA = new Porta();
	public static final VigenereType PORTA_VARIANT = new PortaVariant();
	public static final VigenereType VARIANT = new Variant();
	public static final VigenereType VIGENERE = new Vigenere();
	public static final VigenereType[] SLIDEFAIR_LIST = new VigenereType[] {BEAUFORT, VARIANT, VIGENERE};
	public static final VigenereType[] NORMAL_LIST = new VigenereType[] {BEAUFORT, PORTA, VARIANT, VIGENERE};
	
	private VigenereType() {} //Can't create any more instances
	
	private static class Beaufort extends VigenereType {
		@Override
		public byte encode(byte textCharId, byte keyCharId) {
			return (byte)((keyCharId - textCharId + 26) % 26 + 'A');
		}
		
		@Override
		public byte decode(byte textCharId, byte keyCharId) {
			return encode(textCharId, keyCharId);
		}
	}
	
	private static class Porta extends VigenereType {
		@Override
		public byte encode(byte textCharId, byte keyCharId) {
			byte key = (byte)((keyCharId - 'A') / 2);
            byte newCharId = textCharId;
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
		public byte decode(byte textCharId, byte keyCharId) {
            return encode(textCharId, keyCharId);
		}
	}
	
	private static class PortaVariant extends VigenereType {
		@Override
		public byte encode(byte textCharId, byte keyCharId) {
			byte key = (byte)((keyCharId - 'A') / 2);
            byte newCharId = textCharId;
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
		public byte decode(byte textCharId, byte keyCharId) {
            return encode(textCharId, keyCharId);
		}
	}
	
	private static class Variant extends VigenereType {
		@Override
		public byte encode(byte textCharId, byte keyCharId) { //'A' - 'A' = 0   mod 26 = 0
			return (byte)((-keyCharId + textCharId + 26) % 26 + 'A');
		}
		
		@Override
		public byte decode(byte textCharId, byte keyCharId) { //'A' + 'A' = 130 mod 26 = 0
			return (byte)((keyCharId + textCharId) % 26 + 'A');
		}
	}
	
	private static class Vigenere extends VigenereType {
		@Override
		public byte encode(byte textCharId, byte keyCharId) { //'A' + 'A' = 130 mod 26 = 0
			return (byte)((keyCharId + textCharId) % 26 + 'A');
		}
		
		@Override
		public byte decode(byte textCharId, byte keyCharId) { //'A' - 'A' = 0   mod 26 = 0
			return (byte)((-keyCharId + textCharId + 26) % 26 + 'A');
		}
	}
	
	public abstract byte encode(byte textCharId, byte keyCharId);
	
	public abstract byte decode(byte textCharId, byte keyCharId);
}
