package otus.study.cashmachine.machine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.TestUtil;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.data.Card;
import otus.study.cashmachine.bank.service.AccountService;
import otus.study.cashmachine.bank.service.impl.CardServiceImpl;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.impl.CashMachineServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashMachineServiceTest {

    @Spy
    @InjectMocks
    private CardServiceImpl cardService;
    @Mock
    private CardsDao cardsDao;
    @Mock
    private AccountService accountService;
    @Mock
    private MoneyBoxService moneyBoxService;
    private CashMachineServiceImpl cashMachineService;
    private final CashMachine cashMachine = new CashMachine(new MoneyBox());
    private Card card;

    @BeforeEach
    void init() {
        cashMachineService = new CashMachineServiceImpl(cardService, accountService, moneyBoxService);
        card = new Card(1L, "2222", 1L, TestUtil.getHash("0000"));
    }


    @Test
    void testGetMoney() {
         // @TODO create get money test using spy as mock

        when(cardsDao.getCardByNumber(anyString())).thenReturn(card);
        when(moneyBoxService.getMoney(cashMachine.getMoneyBox(), 7600)).thenReturn(spy(List.of(1, 2, 1, 1)));

        assertEquals(List.of(1, 2, 1, 1),
                cashMachineService.getMoney(cashMachine, "2222", "0000", BigDecimal.valueOf(7600)));
        assertNotEquals(List.of(1, 2, 2, 1),
                cashMachineService.getMoney(cashMachine, "2222", "0000", BigDecimal.valueOf(7600)));
    }

    @Test
    void testPutMoney() {
        when(cardsDao.getCardByNumber(anyString())).thenReturn(card);
        when(cardService.putMoney("2222", "0000", BigDecimal.valueOf(6600)))
                .thenReturn(BigDecimal.valueOf(5500));

        assertEquals(cashMachineService.putMoney(cashMachine, "2222", "0000", List.of(1, 1, 1, 1)),
                new BigDecimal(5500));
        assertNotEquals(cashMachineService.putMoney(cashMachine, "2222", "0000", List.of(1, 1, 1, 1)),
                new BigDecimal(6600));
    }

    @Test
    void testCheckBalance() {
        when(cardsDao.getCardByNumber(anyString())).thenReturn(card);
        when(cardService.getBalance("2222", "0000")).thenReturn(BigDecimal.TEN);

        assertEquals(BigDecimal.TEN, cashMachineService.checkBalance(cashMachine, "2222", "0000"));
        assertNotEquals(BigDecimal.ONE, cashMachineService.checkBalance(cashMachine, "2222", "0000"));
    }

    @Test
    void testChangePin() {
         // @TODO create change pin test using spy as implementation and ArgumentCaptor and thenReturn

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(cardsDao.getCardByNumber(captor.capture())).thenReturn(card);
        when(cardService.changePin("2222", "0000", "3333")).thenReturn(true);

        assertTrue(cashMachineService.changePin("2222", "0000", "3333"));
        assertFalse(cashMachineService.changePin("1111", "0000", "3333"));
    }

    @Test
    void testChangePinWithAnswer() {
        // @TODO create change pin test using spy as implementation and mock an thenAnswer

        when(cardsDao.getCardByNumber(anyString())).thenReturn(card);
        when(cardService.changePin("2222", "0000", "3333")).thenAnswer(answer -> true);

        assertTrue(cashMachineService.changePin("2222", "0000", "3333"));
        assertFalse(cashMachineService.changePin("1111", "0000", "3333"));
    }
}