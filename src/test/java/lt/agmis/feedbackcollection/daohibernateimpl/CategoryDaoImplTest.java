package lt.agmis.feedbackcollection.daohibernateimpl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import lt.agmis.feedbackcollection.dao.CategoryDao;
import lt.agmis.feedbackcollection.domain.Category;
import org.dbunit.IDatabaseTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;


@ContextConfiguration({"classpath:beans/context/root-context.xml", "classpath:beans/context/servlet-context.xml"})
@ActiveProfiles("db-integration-test")
@TransactionConfiguration
@Test
public class CategoryDaoImplTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource(name="databaseTester")
    private IDatabaseTester databaseTester;

    @Autowired
    private CategoryDao sut;

    private Category category;

    @BeforeMethod
    public void setUp() {
        category = new Category();
        category.setName("Any Category");
    }

    public void shouldInsertCategory() {
        Category createdCategory = sut.storeCategory(category);
        Category insertedCategory = findItemWithId(sut.getCategories(), createdCategory.getId());
        assertNotNull(insertedCategory);
    }

    public void shouldDeleteCategory() {
        Category insertedCategory = sut.storeCategory(category);
        sut.deleteCategory(insertedCategory);
        Category deletedCategory = findItemWithId(sut.getCategories(), insertedCategory.getId());
        assertNull(deletedCategory);
    }

    public void shouldUpdateCategory() {
        category.setName("Name");
        Category insertedCategory = sut.storeCategory(category);
        insertedCategory.setName("UpdatedName");

        Category updatedCategory = findItemWithId(sut.getCategories(), insertedCategory.getId());
        assertEquals(updatedCategory.getName(), "UpdatedName");
    }

    private Category findItemWithId(List<Category> categoryList, final int id) {
        return Iterables.find(categoryList, new Predicate<Category>() {
            @Override
            public boolean apply(Category category) {
                return category.getId() == id;
            }
        }, null);
    }
}
