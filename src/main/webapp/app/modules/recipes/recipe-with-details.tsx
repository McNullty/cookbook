import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './recipes.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RecipeWithDetails = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const recipeEntity = useAppSelector(state => state.recipeWithDetail.entity);
  const loading = useAppSelector(state => state.recipeWithDetail.loading);
  const recipeItems = recipeEntity.recipeItems;

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recipeDetailsHeading">
          <Translate contentKey="cookbookApp.recipe.detail.title">Recipe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="cookbookApp.recipe.name">Name</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.name}</dd>
          <dt>
            <span id="recipe-items">
              <Translate contentKey="cookbookApp.recipe.ingredients">Ingredients</Translate>
            </span>
          </dt>
          <dd>
            <div className="table-responsive">
              {recipeItems && recipeItems.length > 0 ? (
                <Table responsive>
                  <thead>
                  <tr>
                    <th>
                      <Translate contentKey="cookbookApp.recipe.ingredientName">Name</Translate>
                    </th>
                    <th>
                      <Translate contentKey="cookbookApp.recipe.ingredientQuantity">Quantity</Translate>
                    </th>
                    <th>
                      <Translate contentKey="cookbookApp.recipe.ingredientUnit">Unit</Translate>
                    </th>
                    <th />
                  </tr>
                  </thead>
                  <tbody>
                  {recipeItems.map((recipeItem, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>{recipeItem.ingredient}</td>
                      <td>{recipeItem.quantity}</td>
                      <td>{recipeItem.unit}</td>
                    </tr>
                  ))}
                  </tbody>
                </Table>
              ) : (
                !loading && (
                  <div className="alert alert-warning">
                    <Translate contentKey="cookbookApp.recipe.home.notFound">No Recipes found</Translate>
                  </div>
                )
              )}
            </div>
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="cookbookApp.recipe.description">Description</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/recipes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/recipes/${recipeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecipeWithDetails;
