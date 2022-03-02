import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './ingredient-for-recipe.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const IngredientForRecipeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const ingredientForRecipeEntity = useAppSelector(state => state.ingredientForRecipe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingredientForRecipeDetailsHeading">
          <Translate contentKey="cookbookApp.ingredientForRecipe.detail.title">IngredientForRecipe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ingredientForRecipeEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="cookbookApp.ingredientForRecipe.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{ingredientForRecipeEntity.quantity}</dd>
          <dt>
            <Translate contentKey="cookbookApp.ingredientForRecipe.ingredient">Ingredient</Translate>
          </dt>
          <dd>{ingredientForRecipeEntity.ingredient ? ingredientForRecipeEntity.ingredient.name : ''}</dd>
          <dt>
            <Translate contentKey="cookbookApp.ingredientForRecipe.unit">Unit</Translate>
          </dt>
          <dd>{ingredientForRecipeEntity.unit ? ingredientForRecipeEntity.unit.name : ''}</dd>
          <dt>
            <Translate contentKey="cookbookApp.ingredientForRecipe.recipe">Recipe</Translate>
          </dt>
          <dd>{ingredientForRecipeEntity.recipe ? ingredientForRecipeEntity.recipe.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/ingredient-for-recipe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ingredient-for-recipe/${ingredientForRecipeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IngredientForRecipeDetail;
