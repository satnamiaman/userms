package com.five.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.five.user.dto.BuyerDTO;
import com.five.user.dto.CartDTO;
import com.five.user.dto.LoginDTO;
import com.five.user.dto.ProductDTO;
import com.five.user.dto.SellerDTO;
import com.five.user.dto.WishlistDTO;
import com.five.user.service.BuyerService;
import com.five.user.service.SellerService;

@RestController
public class UserController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${product.uri}")
	String productUri;

	@Autowired
	BuyerService buyerService;

	@Autowired
	SellerService sellerService;

	@Autowired
	Environment environment;
	
	//Register Buyer 
	@PostMapping(value = "/api/buyer/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createBuyer(@Valid @RequestBody BuyerDTO buyerDTO) throws Exception{
		
		logger.info("Creation request for customer {}", buyerDTO);
		String successMessage = "Added Successfully " + buyerService.saveBuyer(buyerDTO);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}

	//Get Buyer through id
	@GetMapping(value = "/api/buyer/{buyerid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BuyerDTO> getBuyerById(@PathVariable String buyerid) throws Exception {
		
		try {
			BuyerDTO buyer = buyerService.getBuyerById(buyerid);
			return new ResponseEntity<>(buyer, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}
	}

	//Buyer Login
	@PostMapping(value = "/buyer/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean login(@RequestBody LoginDTO loginDTO) {
		
		logger.info("Login request for customer {} with password {}", loginDTO.getEmail(),loginDTO.getPassword());
		return buyerService.login(loginDTO);
	}

	//Delete Buyer
	@DeleteMapping(value = "/buyer/{buyerid}")
	public ResponseEntity<String> deactivateBuyer(@PathVariable String buyerid) throws Exception {
		
		String successMessage = buyerService.deactivateBuyer(buyerid);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}

	//Add products to cart
	@PostMapping(value = "/api/cart/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addCart(@RequestBody CartDTO cartDTO) throws Exception {
		ProductDTO prod = new RestTemplate().getForObject(productUri+ "/productid/" + cartDTO.getProdid(), ProductDTO.class);
		if(prod.getStock() > cartDTO.getQuantity())
		{
		logger.info("Additing to cart", cartDTO);
		String successMessage  = buyerService.addToCart(cartDTO);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
		}
		else
		{
			String successMessage  = "Out of Stock";
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		}
	}

	//Remove from cart
	@DeleteMapping(value = "/api/cart/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> RemoveCart(@RequestBody CartDTO cartDTO) throws Exception {
		
		logger.info("Removing from cart", cartDTO);
		String successMessage  = buyerService.removeFromCart(cartDTO);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}

	//Move from wishlist to cart
	@PostMapping(value = "/api/wishlist/move", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> moveToCart(@RequestBody CartDTO cartDTO) throws Exception {
		ProductDTO prod = new RestTemplate().getForObject(productUri+ "/productid/" + cartDTO.getProdid(), ProductDTO.class);
		if(prod.getStock() > cartDTO.getQuantity())
		{
		logger.info("Moving to cart", cartDTO);
		WishlistDTO wishDTO = new WishlistDTO();
		wishDTO.setBuyerid(cartDTO.getBuyerid());
		wishDTO.setProdid(cartDTO.getProdid());
		String successMessage  = buyerService.addToCart(cartDTO);
		buyerService.removeFromWishlist(wishDTO);
		String successMsg = new RestTemplate().patchForObject(productUri + "api/product/"+ cartDTO.getProdid()+"/"+ (~(cartDTO.getQuantity() - 1)), null, String.class);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
		
		}
		else
		{
			String successMessage  = "Out of Stock";
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		}
	}
	
	//get buyer's cart detail
	@GetMapping(value = "/api/cart/{buyerid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CartDTO> getCartDetail(@PathVariable String buyerid) {
		logger.info("Fetching all Buyers");
		return buyerService.getCart(buyerid);
	}

	//Adding to wishlist
	@PostMapping(value = "/api/wishlist/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addWishlist(@RequestBody WishlistDTO wishDTO) throws Exception {
		
		logger.info("Additing to Wishlist ", wishDTO);
		String successMessage  = buyerService.addToWishlist(wishDTO);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}

	//Deleting from the wishlist
	@DeleteMapping(value = "/api/wishlist/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> RemoveFromWishlist(@RequestBody WishlistDTO wishDTO) throws Exception {
		
		logger.info("Removing from Wishlist {}", wishDTO);
		String successMessage  = buyerService.removeFromWishlist(wishDTO);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}


	//Seller	

	//Register Seller Details
	@PostMapping(value = "/api/seller/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createSeller(@Valid @RequestBody SellerDTO sellerDTO) throws Exception{
		
		logger.info("Creation request for Seller {}", sellerDTO);
		String st = "Seller account created successfully" + sellerService.saveSeller(sellerDTO);
		return new ResponseEntity<>(st, HttpStatus.OK);
	}

	//All sellers details
	@GetMapping(value = "/api/sellers", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SellerDTO> getAllSeller() {
		
		logger.info("Fetching all sellers {}");
		return sellerService.getAllSeller();
	}

	//seller login
	@PostMapping(value = "/seller/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean Login(@RequestBody LoginDTO loginDTO) {
		
		logger.info("Login request for seller {} with password {}", loginDTO.getEmail(),loginDTO.getPassword());
		return sellerService.login(loginDTO);
	}


	//Delete seller account
	@DeleteMapping(value = "/seller/{sellerid}")
	public ResponseEntity<String> deleteSeller(@PathVariable String sellerid) throws Exception {
		
		String successMessage  = sellerService.deleteSeller(sellerid);
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}


	//Getting All buyers details
	@GetMapping(value = "/api/buyers", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BuyerDTO> getAllBuyer() {
		
		logger.info("Fetching all Buyers");
		return buyerService.getAllBuyer();
	}

}
