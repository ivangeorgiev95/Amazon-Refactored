import com.amazon.domain.Category;
import com.amazon.repository.CategoryRepository;
import com.amazon.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AmazonApplication.class)
public class CategoryServiceTest {

//    @Autowired
//    private CategoryService categoryService;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Before
//    public void clear(){
//        categoryRepository.deleteAllInBatch();
//    }
//
//    @Test
//    public void findParentCategoryTest(){
//        Category parentCategory = new Category();
//        parentCategory.setName("Parent category");
//        categoryRepository.save(parentCategory);
//        List<Category> result = categoryService.getAllParentCategories();
//        assertTrue( result.size() == 1 && result.get(0).getName().equals("Parent category") && result.get(0).getParentCategory() == null);
//    }
//
//    @Test
//    public void findAllCategoriesTest(){
//        Category parentCategory = new Category();
//        parentCategory.setName("Parent category");
//        Category childCategory = new Category();
//        childCategory .setName("Child category");
//        childCategory.setParentCategory(parentCategory);
//        categoryRepository.save(parentCategory);
//        categoryRepository.save(childCategory);
//        List<Category> result = categoryService.getAllCategories();
//        assertTrue(result.size() == 2 && result.get(0).getName().equals("Parent category") && result.get(1).getName().equals("Child category"));
//    }

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void findParentCategoryTest(){
        Category parentCategory = new Category();
        parentCategory.setName("Parent category");
        when(categoryRepository.findByParentCategoryNull()).thenReturn(Arrays.asList(parentCategory));
        List<Category> result = categoryService.getAllParentCategories();
        assertTrue( result.size() == 1 && result.get(0).getName().equals("Parent category") && result.get(0).getParentCategory() == null);
    }

    @Test
    public void findAllCategoriesTest(){
        Category parentCategory = new Category();
        parentCategory.setName("Parent category");
        Category childCategory = new Category();
        childCategory .setName("Child category");
        childCategory.setParentCategory(parentCategory);
       when(categoryRepository.findAll()).thenReturn(Arrays.asList(parentCategory, childCategory));
        List<Category> result = categoryService.getAllCategories();
        assertTrue(result.size() == 2 && result.get(0).getName().equals("Parent category") && result.get(1).getName().equals("Child category"));
    }

}
