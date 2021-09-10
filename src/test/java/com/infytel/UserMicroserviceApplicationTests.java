package com.infytel;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.five.user.UserApplication;
import com.five.user.dto.BuyerDTO;
import com.five.user.dto.LoginDTO;
import com.five.user.repository.BuyerRepository;
import com.five.user.service.BuyerService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = UserApplication.class)
public class UserMicroserviceApplicationTests {


	@Mock
	BuyerRepository buyerRepo;

	@InjectMocks
	BuyerService buyerService =  new BuyerService();

	@Test
	public void authenticateCustomerTestValidCredentials() throws Exception {
		LoginDTO buyer = new LoginDTO();
		buyer.setEmail("vipatilraj@gmail.com");
		buyer.setPassword("Vicky@9009");
		//Mockito.when(buyerRepo.findByEmail("vipatilraj@gmail.com")).thenReturn(buyer);
		boolean actual = buyerService.login(buyer);
		Assertions.assertThat(actual);
	} 
	
	@Test
	public void authenticateCustomerTestInValidCredentials() throws Exception {
		LoginDTO customer = new LoginDTO();
		customer.setEmail("vipatilraj@gmail.com");
		customer.setPassword("Vicky@9");
		//Mockito.when(buyerRepo.findByEmail("vipatilraj@gmail.com")).thenReturn(buyer);
		boolean actual = buyerService.login(customer);
		Assertions.assertThat(!actual);
	} 
}

