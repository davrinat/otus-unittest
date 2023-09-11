package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.impl.AccountServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountDao accountDao;
    private AccountServiceImpl accountService;
    @Mock
    private Account account;

    @BeforeEach
    public void init() {
        account = new Account(5L, BigDecimal.valueOf(20000));
        accountService = new AccountServiceImpl(accountDao);
    }

    @Test
    void createAccountMock() {
         // @TODO test account creation with mock and ArgumentMatcher

        doAnswer(answer -> account).when(accountDao).saveAccount(any());
        var newAccount = accountService.createAccount(BigDecimal.valueOf(9000));

        assertEquals(account.getId(), newAccount.getId());
        assertEquals(account.getAmount(), newAccount.getAmount());
    }

    @Test
    void createAccountCaptor() {
         //  @TODO test account creation with ArgumentCaptor

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        doAnswer(answer -> account).when(accountDao).saveAccount(captor.capture());

        var newAccount = accountService.createAccount(BigDecimal.valueOf(9000));

        verify(accountDao, only()).saveAccount(any());

        assertEquals(account.getId(), newAccount.getId());
        assertEquals(account.getAmount(), newAccount.getAmount());
    }

    @Test
    void addSum() {
        when(accountDao.getAccount(anyLong())).thenReturn(account);

        assertEquals(-1, account.getAmount().compareTo(accountService.putMoney(10L, BigDecimal.valueOf(4000))));
    }

    @Test
    void getSum() {
        when(accountDao.getAccount(anyLong())).thenReturn(account);

        assertEquals(BigDecimal.valueOf(17000), accountService.getMoney(3L, BigDecimal.valueOf(3000)));
    }

    @Test
    void getAccount() {
        when(accountDao.getAccount(anyLong())).thenReturn(account);

        assertEquals(BigDecimal.valueOf(20000), accountService.getAccount(2L).getAmount());
        assertEquals(5L, accountService.getAccount(2L).getId());
    }

    @Test
    void checkBalance() {
        when(accountDao.getAccount(anyLong())).thenReturn(account);

        assertEquals(BigDecimal.valueOf(20000), accountService.checkBalance(1L));
    }
}
