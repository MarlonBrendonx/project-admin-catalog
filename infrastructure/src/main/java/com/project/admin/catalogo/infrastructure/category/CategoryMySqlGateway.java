package com.project.admin.catalogo.infrastructure.category;


import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.category.CategorySearchQuery;
import com.project.admin.catalogo.domain.pagination.Pagination;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;


import static com.project.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

@Component
public class CategoryMySqlGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySqlGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deletedById(final CategoryID anID) {
        final  String anIdValue = anID.getValue();
        if(this.categoryRepository.existsById(anIdValue)){
            this.categoryRepository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID anID) {
        return this.categoryRepository.findById(anID.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }


    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        // Paginação
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        // Busca dinamica pelo criterio terms (name ou description)
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult =
                this.categoryRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

//    @Override
//    public List<CategoryID> existsByIds(final Iterable<CategoryID> categoryIDs) {
//        final var ids = StreamSupport.stream(categoryIDs.spliterator(), false)
//                .map(CategoryID::getValue)
//                .toList();
//        return this.categoryRepository.existsByIds(ids).stream()
//                .map(CategoryID::from)
//                .toList();
//    }


    private Category save(Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    private Specification<CategoryJpaEntity> assembleSpecification(final String str) {
        final Specification<CategoryJpaEntity> nameLike = like("name", str);
        final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
        return nameLike.or(descriptionLike);
    }

}
