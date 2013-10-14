package lt.agmis.feedbackcollection.daohibernateimpl;

import lt.agmis.feedbackcollection.dao.CategoryDao;
import lt.agmis.feedbackcollection.domain.Category;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
     public List<Category> getCategories() {
        List<Category> categories = (List<Category>) sessionFactory.getCurrentSession().createCriteria(Category.class).list();
        return categories;
    }

    @Override
    public Category storeCategory(Category category) {
        sessionFactory.getCurrentSession().save(category);
        return category;
    }

    @Override
    public void deleteCategory(Category category) {
        sessionFactory.getCurrentSession().delete(category);
    }
}
