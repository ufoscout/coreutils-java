package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.*;
import org.junit.jupiter.api.Test;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public class JSR303ValidatorServiceTest extends BaseTest {

	private final JSR303ValidatorService validationService = new JSR303ValidatorService();

	@Test
	public void testBeanValidation() {
		Song song = new Song();
		song.setTitle("u");
		song.setYear(Integer.valueOf(100));

		ValidationRule<Song> customValidationRule = new ValidationRule<Song>() {

			@Override
			public void validate(final Song data, final ViolationManager violationManager) {
				if ((data.getArtist() == null) || !data.getArtist().equals("Queen")) {
					violationManager.addViolation("bad_artist", "Artist is not Queen!");
				}
			}
		};

		ValidationResult<Song> validationResult = validationService.validator(customValidationRule).validate(song);
		assertEquals( song , validationResult.getValidatedBean() );
		Map<String, List<String>> errors = validationResult.getViolations();
		assertNotNull(errors);
		printErrors(errors);

		assertTrue( errors.containsKey("bad_artist") );
		assertTrue( errors.get("bad_artist").contains("Artist is not Queen!") );

		assertTrue( errors.containsKey("title") );
		assertTrue( errors.get("title").size() == 1 );
		assertTrue( errors.get("title").contains("minLenght3") );

		assertTrue( errors.containsKey("artist") );
		assertTrue( errors.get("artist").size() == 1 );
		assertTrue( errors.get("artist").contains("notNull") );

		assertTrue( errors.containsKey("year") );
		assertTrue( errors.get("year").size() == 1 );
		assertTrue( errors.get("year").contains("minSize1900") );
	}

	@Test
	public void testComplexBeanValidation() {
		BeanOne beanOne = new BeanOne();
		beanOne.beanOneList.add(new BeanOne());

		BeanOne beanOneValid = new BeanOne();
		beanOneValid.value = "";
		beanOne.beanOneList.add(beanOneValid);

		beanOne.beanOneList.add(new BeanOne());

		BeanOne beanTwoValid = new BeanOne();
		beanTwoValid.beanOneList.add(new BeanOne());
		beanTwoValid.value = "";
		beanOne.beanOneList.add(beanTwoValid);

		beanOne.innerBean = new BeanOne();
		beanOne.innerBean.beanOneList.add(new BeanOne());

		ValidationResultImpl<BeanOne> validationResult = validationService.<BeanOne>validator().validate(beanOne);
		assertEquals( beanOne,  validationResult.getValidatedBean() );

		Map<String, List<String>> errors = validationResult.getViolations();
		printErrors(errors);
		assertTrue( errors.containsKey("value") );
		assertTrue( errors.containsKey("beanOneList[0].value") );
		assertTrue( errors.containsKey("beanOneList[2].value") );
		assertTrue( errors.containsKey("beanOneList[3].beanOneList[0].value") );
		assertTrue( errors.containsKey("innerBean.value") );
		assertTrue( errors.containsKey("innerBean.beanOneList[0].value") );
	}

	private void printErrors(final Map<String, List<String>> errors) {
		System.out.println("-------------------------------------");
		for (Entry<String, List<String>> errorEntry :  errors.entrySet()) {
			String key = errorEntry.getKey();
			for (String message : errorEntry.getValue()) {
				System.out.println("Violation found -> key [" + key + "] - [" + message + "]");
			}
		}
		System.out.println("-------------------------------------");
	}

	@SuppressWarnings("nls")
	@Test
	public void testBeanGroup() {
		HelloBean bean = new HelloBean();

		ValidationResultImpl<HelloBean> resultWithoutGroup = validationService.<HelloBean>validator().validate(bean);
		assertNotNull( resultWithoutGroup.getViolations().get("hello") );
		assertNotNull( resultWithoutGroup.getViolations().get("helloTwo") );
		assertNull( resultWithoutGroup.getViolations().get("helloProfile") );

		ValidationResult<HelloBean> resultWithGroup = validationService.<HelloBean>validator().groups(BeanCheck.class).validate(bean);
		assertNull( resultWithGroup.getViolations().get("hello") );
		assertNotNull( resultWithGroup.getViolations().get("helloProfile") );

		ValidationResult<HelloBean> resultWithMoreGroups = validationService.<HelloBean>validator().groups(Default.class, BeanCheck.class).validate(bean);
		assertNotNull( resultWithMoreGroups.getViolations().get("hello") );
		assertNotNull( resultWithMoreGroups.getViolations().get("helloProfile") );

	}

	@SuppressWarnings("nls")
	@Test
	public void testValidateBeanProperty() {

		HelloBean bean = new HelloBean();

		ValidationResultImpl<HelloBean> resultWithoutGroup = validationService.<HelloBean>validator().validateProperty(bean,"helloTwo");
		assertNull( resultWithoutGroup.getViolations().get("hello") );
		assertNotNull( resultWithoutGroup.getViolations().get("helloTwo") );

	}

	@Test
	public void testValidateBeanWithMap() {

		BeanWithMap bean = new BeanWithMap();
		bean.beanOneMap.put("key1", new BeanOne());
		bean.beanOneMap.put("key2", new BeanOne());
		bean.beanOneMap.put("keyValid1", new BeanOne());

		bean.beanOneMap.get("keyValid1").value = "good value";

		ValidationResultImpl<BeanWithMap> result = validationService.<BeanWithMap>validator().validate(bean);
		getLogger().info("" + result.getViolations());

		assertTrue(result.getViolations().containsKey("beanOneMap[key1].value"));
		assertTrue(result.getViolations().containsKey("beanOneMap[key2].value"));
		assertFalse(result.getViolations().containsKey("beanOneMap[keyValid1].value"));

	}

}
