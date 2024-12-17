import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ComputerTest {
    @Test
    fun test1 () {
        val register = Register(0,0,9)
        val underTest = Computer(
            register,
            listOf(2,1)
        )
        underTest.execute()
        assertEquals(1, register.B)
    }

    @Test
    fun test2 () {
        val register = Register(10,0,0)
        val underTest = Computer(
            register,
            listOf(5,0,5,1,5,4)
        )
        val output = underTest.execute()
        assertEquals("0,1,2", output)
    }

    @Test
    fun test3 () {
        val register = Register(2024,0,0)
        val underTest = Computer(
            register,
            listOf(0,1,5,4,3,0)
        )
        val output = underTest.execute()
        assertEquals("4,2,5,6,7,7,7,7,3,1,0", output)
        assertEquals(0, register.A)
    }

    @Test
    fun test4 () {
        val register = Register(0,29,0)
        val underTest = Computer(
            register,
            listOf(1,7)
        )
        val output = underTest.execute()
        assertEquals(26, register.B)
    }

    @Test
    fun test5 () {
        val register = Register(0,2024,43690)
        val underTest = Computer(
            register,
            listOf(4,0)
        )
        val output = underTest.execute()
        assertEquals(44354, register.B)
    }
}