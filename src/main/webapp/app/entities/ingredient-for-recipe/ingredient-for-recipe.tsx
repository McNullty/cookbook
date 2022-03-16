import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './ingredient-for-recipe.reducer';
import { IIngredientForRecipe } from 'app/shared/model/ingredient-for-recipe.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const IngredientForRecipe = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const ingredientForRecipeList = useAppSelector(state => state.ingredientForRecipe.entities);
  const loading = useAppSelector(state => state.ingredientForRecipe.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="ingredient-for-recipe-heading" data-cy="IngredientForRecipeHeading">
        <Translate contentKey="cookbookApp.ingredientForRecipe.home.title">Ingredient For Recipes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cookbookApp.ingredientForRecipe.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cookbookApp.ingredientForRecipe.home.createLabel">Create new Ingredient For Recipe</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ingredientForRecipeList && ingredientForRecipeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="cookbookApp.ingredientForRecipe.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="cookbookApp.ingredientForRecipe.recipe">Recipe</Translate>
                </th>
                <th>
                  <Translate contentKey="cookbookApp.ingredientForRecipe.ingredient">Ingredient</Translate>
                </th>
                <th>
                  <Translate contentKey="cookbookApp.ingredientForRecipe.quantity">Quantity</Translate>
                </th>
                <th>
                  <Translate contentKey="cookbookApp.ingredientForRecipe.unit">Unit</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ingredientForRecipeList.map((ingredientForRecipe, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${ingredientForRecipe.id}`} color="link" size="sm">
                      {ingredientForRecipe.id}
                    </Button>
                  </td>
                  <td>
                    {ingredientForRecipe.recipe ? (
                      <Link to={`recipe/${ingredientForRecipe.recipe.id}`}>{ingredientForRecipe.recipe.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {ingredientForRecipe.ingredient ? (
                      <Link to={`ingredient/${ingredientForRecipe.ingredient.id}`}>{ingredientForRecipe.ingredient.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{ingredientForRecipe.quantity}</td>
                  <td>
                    {ingredientForRecipe.unit ? (
                      <Link to={`unit/${ingredientForRecipe.unit.id}`}>{ingredientForRecipe.unit.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${ingredientForRecipe.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${ingredientForRecipe.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${ingredientForRecipe.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cookbookApp.ingredientForRecipe.home.notFound">No Ingredient For Recipes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default IngredientForRecipe;
