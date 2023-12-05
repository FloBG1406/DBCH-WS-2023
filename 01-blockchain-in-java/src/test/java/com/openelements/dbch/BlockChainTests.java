package com.openelements.dbch;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BlockChainTests {

    @Test
    void testEmptyChain() {
        //given
        Chain chain = new Chain();

        //then
        Assertions.assertDoesNotThrow(() -> chain.verify());
    }

    @Test
    void testChainWithOneElement() {
        //given
        Chain chain = new Chain();
        String data = "Hello World";

        //when
        chain.addBlock(data);

        //then
        Assertions.assertDoesNotThrow(() -> chain.verify());
    }

    @Test
    void testChainWithManyElements() {
        //given
        Chain chain = new Chain();
        String data1 = "Hello World";
        String data2 = "bla";
        String data3 = "test";
        String data4 = "123";

        //when
        chain.addBlock(data1);
        chain.addBlock(data2);
        chain.addBlock(data3);
        chain.addBlock(data4);

        //then
        Assertions.assertDoesNotThrow(() -> chain.verify());
    }

    @Test
    void testBadBlockchain() {
        //given
        Chain chain = new Chain();
        String data1 = "Hello World";
        String data2 = "bla";
        String data3 = "test";
        String data4 = "123";

        //when
        chain.addBlock(data1);
        chain.addBlock(data2);
        chain.addBlock(data3);
        chain.addBlock(data4);
        removeBlock(chain, 2);

        //then
        Assertions.assertThrows(IllegalStateException.class, () -> chain.verify());
    }

    @Test
    void testBadFirstBlock() {
        //given
        Chain chain = new Chain();
        String data1 = "Hello World";
        String data2 = "bla";

        //when
        chain.addBlock(data1);
        chain.addBlock(data2);
        removeBlock(chain, 0);

        //then
        Assertions.assertThrows(IllegalStateException.class, () -> chain.verify());
    }

    private static void removeBlock(Chain chain, int index) {
        try {
            final Field blocksField = Chain.class.getDeclaredField("blocks");
            blocksField.setAccessible(true);
            final List<Block> innerList = (List<Block>) blocksField.get(chain);
            innerList.remove(index);
        } catch (Exception e) {
            throw new RuntimeException("hack broken", e);
        }
    }
}
