package cn.bywei.weixin;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.bywei.weixin.service.DefaultService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartApplication.class)
@WebAppConfiguration
public class DefaultServiceTest {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
	private MockMvc mockMvc;

	@Autowired
    private WebApplicationContext webApplicationContext ;

	@Autowired
	private DefaultService secretKeyService;
	
	@Before
    public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
	
	@Test
	public void test_api_getSecretKey_should_success() throws UnsupportedEncodingException, Exception {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/msg/sendMsg");
        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder).andDo(print());
        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
		Assert.assertNotNull(result.getResponse().getContentAsString());
		Assert.assertTrue(result.getResponse().getContentAsString().contains("123456"));
	}
}
