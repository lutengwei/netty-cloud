package com.lutw.common.core.bean;

//16进制枚举类
public enum HexadecimalEnum {
    //可扩展添加
    ONE(1,(byte)0x01),TWO(2,(byte)0x02),THREE(3,(byte)0x03),
    FOUR(4,(byte)0x04),FIVE(5,(byte)0x05),SIX(6,(byte)0x06),
    SEVEN(7,(byte)0x07),EIGHT(8,(byte)0x08),NINE(9,(byte)0x09),
    NUMBERA(10,(byte)0x0A),NUMBERB(11,(byte)0x0B),NUMBERC(12,(byte)0x0C),
    NUMBERD(13,(byte)0x0D),NUMBERE(14,(byte)0x0E),NUMBERF(15,(byte)0x0F),SIXTEEN(16,(byte)0x10);



    private int id;
    private byte code;

    HexadecimalEnum(int id, byte code) {
        this.id = id;
        this.code = code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public static HexadecimalEnum getById(int id){
        for (HexadecimalEnum htype:values()) {
            if(htype.getId() == id){
                return htype;
            }
        }

        return null;
    }
}
