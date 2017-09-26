import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

public class Blabla {

    private final void method1013(StreamBuffer buf, int opcode) {
        if (opcode != 1) {
            if (opcode != 2) {
                if (opcode != 4) {
                    do {
                        if (opcode != 5) {
                            if (opcode == 6)
                                ((Class94) this).anInt1477 = buf.readUnsignedShort();
                            else {
                                if (opcode != 7) {
                                    if (opcode != 8) {
                                        if (opcode == 11)
                                            ((Class94) this).anInt1456 = 1;
                                        else if (opcode != 12) {
                                            if (opcode != 16) {
                                                if (opcode == 23)
                                                    ((Class94) this).anInt1424 = (buf.readUnsignedShort());
                                                else if (opcode != 24) {
                                                    if (opcode == 25)
                                                        ((Class94) this).anInt1487 = (buf.readUnsignedShort());
                                                    else if (opcode == 26)
                                                        anInt1435 = (buf.readUnsignedShort());
                                                    else if (opcode < 30 || opcode >= 35) {
                                                        if (opcode >= 35 && opcode < 40)
                                                            ((Class94) this).aStringArray1475[-35 + opcode] = (buf.readString());
                                                        else if (opcode == 40) {
                                                            int i_48_ = (buf.readUnsignedByte());
                                                            aShortArray1457 = (new short[i_48_]);
                                                            aShortArray1492 = (new short[i_48_]);
                                                            for (int i_49_ = 0; i_49_ < i_48_; i_49_++) {
                                                                aShortArray1457[i_49_] = (short) buf.readUnsignedShort();
                                                                aShortArray1492[i_49_] = (short) buf.readUnsignedShort();
                                                            }
                                                        } else if (opcode != 41) {
                                                            if (opcode == 42) {
                                                                int i_50_ = (buf.readUnsignedByte());
                                                                aByteArray1501 = (new byte[i_50_]);
                                                                for (int i_51_ = 0; (i_51_ < i_50_); i_51_++) aByteArray1501[i_51_] = (buf.readByte(false));
                                                            } else if (opcode != 65) {
                                                                if (opcode == 78)
                                                                    anInt1479 = (buf.readUnsignedShort());
                                                                else if (opcode == 79)
                                                                    anInt1438 = (buf.readUnsignedShort());
                                                                else if (opcode != 90) {
                                                                    if (opcode != 91) {
                                                                        if (opcode == 92)
                                                                            anInt1450 = buf.readUnsignedShort();
                                                                        else if (opcode != 93) {
                                                                            if (opcode != 95) {
                                                                                if (opcode != 96) {
                                                                                    if (opcode == 97)
                                                                                        ((Class94) this).anInt1474 = buf.readUnsignedShort();
                                                                                    else if (opcode == 98)
                                                                                        ((Class94) this).anInt1500 = buf.readUnsignedShort();
                                                                                    else if (opcode < 100 || opcode >= 110) {
                                                                                        if (opcode == 110)
                                                                                            anInt1423 = buf.readUnsignedShort();
                                                                                        else if (opcode != 111) {
                                                                                            if (opcode == 112)
                                                                                                anInt1480 = buf.readUnsignedShort();
                                                                                            else if (opcode != 113) {
                                                                                                if (opcode == 114)
                                                                                                    anInt1439 = buf.readByte(false) * 5;
                                                                                                else if (opcode == 115)
                                                                                                    ((Class94) this).anInt1462 = buf.readUnsignedByte();
                                                                                                else if (opcode != 121) {
                                                                                                    if (opcode != 122) {
                                                                                                        if (opcode == 125) {
                                                                                                            anInt1493 = buf.readByte(false) << 2;
                                                                                                            anInt1465 = buf.readByte(false) << 2;
                                                                                                            anInt1437 = buf.readByte(false) << 2;
                                                                                                        } else if (opcode == 126) {
                                                                                                            anInt1498 = buf.readByte(false) << 2;
                                                                                                            anInt1470 = buf.readByte(false) << 2;
                                                                                                            anInt1446 = buf.readByte(false) << 2;
                                                                                                        } else if (opcode == 127) {
                                                                                                            ((Class94) this).anInt1455 = buf.readUnsignedByte();
                                                                                                            ((Class94) this).anInt1426 = buf.readUnsignedShort();
                                                                                                        } else if (opcode != 128) {
                                                                                                            if (opcode == 129) {
                                                                                                                ((Class94) this).anInt1433 = buf.readUnsignedByte();
                                                                                                                ((Class94) this).anInt1468 = buf.readUnsignedShort();
                                                                                                            } else if (opcode == 130) {
                                                                                                                ((Class94) this).anInt1440 = buf.readUnsignedByte();
                                                                                                                ((Class94) this).anInt1483 = buf.readUnsignedShort();
                                                                                                            } else if (opcode != 132) {
                                                                                                                if (opcode == 249) {
                                                                                                                    int i_52_ = buf.readUnsignedByte();
                                                                                                                    if (((Class94) this).aClass194_1472 == null) {
                                                                                                                        int i_53_ = Class307.calculateSize(i_52_);
                                                                                                                        ((Class94) this).aClass194_1472 = new HashTable(i_53_);
                                                                                                                    }
                                                                                                                    for (int i_54_ = 0; i_54_ < i_52_; i_54_++) {
                                                                                                                        boolean bool = buf.readUnsignedByte() == 1;
                                                                                                                        int i_55_ = buf.method2507(125);
                                                                                                                        Node class279;
                                                                                                                        if (bool)
                                                                                                                            class279 = new StringNode(buf.readString());
                                                                                                                        else
                                                                                                                            class279 = new IntegerNode(buf.readInt());
                                                                                                                        ((Class94) this).aClass194_1472.add((long) i_55_, class279);
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                int i_56_ = buf.readUnsignedByte();
                                                                                                                ((Class94) this).anIntArray1441 = new int[i_56_];
                                                                                                                for (int i_57_ = 0; i_56_ > i_57_; i_57_++) ((Class94) this).anIntArray1441[i_57_] = buf.readUnsignedShort();
                                                                                                            }
                                                                                                        } else {
                                                                                                            ((Class94) this).anInt1442 = buf.readUnsignedByte();
                                                                                                            ((Class94) this).anInt1476 = buf.readUnsignedShort();
                                                                                                        }
                                                                                                    } else
                                                                                                        ((Class94) this).anInt1431 = buf.readUnsignedShort();
                                                                                                } else
                                                                                                    ((Class94) this).anInt1429 = buf.readUnsignedShort();
                                                                                            } else
                                                                                                anInt1458 = buf.readByte(false);
                                                                                        } else
                                                                                            anInt1503 = buf.readUnsignedShort();
                                                                                    } else {
                                                                                        if (((Class94) this).anIntArray1460 == null) {
                                                                                            ((Class94) this).anIntArray1460 = new int[10];
                                                                                            ((Class94) this).anIntArray1445 = new int[10];
                                                                                        }
                                                                                        ((Class94) this).anIntArray1460[-100 + opcode] = buf.readUnsignedShort();
                                                                                        ((Class94) this).anIntArray1445[opcode - 100] = buf.readUnsignedShort();
                                                                                    }
                                                                                } else
                                                                                    ((Class94) this).anInt1443 = buf.readUnsignedByte();
                                                                            } else
                                                                                ((Class94) this).anInt1494 = buf.readUnsignedShort();
                                                                        } else
                                                                            anInt1490 = buf.readUnsignedShort();
                                                                    } else
                                                                        anInt1466 = buf.readUnsignedShort();
                                                                } else
                                                                    anInt1454 = (buf.readUnsignedShort());
                                                            } else
                                                                ((Class94) this).aBoolean1463 = true;
                                                        } else {
                                                            int i_58_ = (buf.readUnsignedByte());
                                                            aShortArray1504 = (new short[i_58_]);
                                                            aShortArray1488 = (new short[i_58_]);
                                                            for (int i_59_ = 0; i_59_ < i_58_; i_59_++) {
                                                                aShortArray1488[i_59_] = (short) buf.readUnsignedShort();
                                                                aShortArray1504[i_59_] = (short) buf.readUnsignedShort();
                                                            }
                                                        }
                                                    } else
                                                        ((Class94) this).aStringArray1485[opcode + -30] = (buf.readString());
                                                } else
                                                    anInt1449 = (buf.readUnsignedShort());
                                            } else
                                                ((Class94) this).aBoolean1502 = true;
                                        } else
                                            ((Class94) this).anInt1473 = (buf.readInt());
                                    } else {
                                        ((Class94) this).anInt1491 = buf.readUnsignedShort();
                                        if (((Class94) this).anInt1491 > 32767)
                                            ((Class94) this).anInt1491 -= 65536;
                                    }
                                } else {
                                    ((Class94) this).anInt1425 = buf.readUnsignedShort();
                                    if (((Class94) this).anInt1425 <= 32767)
                                        break;
                                    ((Class94) this).anInt1425 -= 65536;
                                }
                                break;
                            }
                            break;
                        }
                        ((Class94) this).anInt1444 = buf.readUnsignedShort();
                    } while (false);
                } else
                    ((Class94) this).anInt1436 = buf.readUnsignedShort();
            } else
                ((Class94) this).aString1434 = buf.readString();
        } else
            anInt1481 = buf.readUnsignedShort();
    }
}
