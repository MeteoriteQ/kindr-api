package com.example.config;
import com.example.Entity.*;
import com.example.dto.*;
import com.example.repository.*;
import com.example.service.PaymentService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class GuestPaymentTest {
 @Test void guestOrderIsAnonymousAndCannotBeConfirmedByAnotherGuest() throws Exception {
  PaymentRepository payments=mock(PaymentRepository.class);
  CampaignRepository campaigns=mock(CampaignRepository.class);
  DonationRepository donations=mock(DonationRepository.class);
  PaymentService service=new PaymentService(payments,donations,campaigns,"","");
  Campaign c=new Campaign();c.setId(1L);c.setStatus("Active");
  when(campaigns.findById(1L)).thenReturn(Optional.of(c));
  CreatePaymentRequest r=new CreatePaymentRequest();r.setCampaignId(1L);r.setAmount(BigDecimal.TEN);
  Payment p=(Payment)service.createOrder(null,r,"guest-one").get("payment");
  assertTrue(p.isAnonymous());assertEquals("guest:guest-one",p.getDonorId());
  when(payments.findByIdForUpdate(p.getId())).thenReturn(Optional.of(p));
  ConfirmPaymentRequest confirm=new ConfirmPaymentRequest();confirm.setPaymentId(p.getId());
  assertThrows(SecurityException.class,()->service.confirm(null,confirm,"guest-two"));
  assertThrows(SecurityException.class,()->service.confirm(null,confirm,null));
 }
}
