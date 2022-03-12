import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './recipe.reducer';
import { IRecipe } from 'app/shared/model/recipe.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Recipe = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const recipeList = useAppSelector(state => state.recipe.entities);
  const loading = useAppSelector(state => state.recipe.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="recipe-heading" data-cy="RecipeHeading">
        <Translate contentKey="cookbookApp.recipe.home.title">Recipes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cookbookApp.recipe.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cookbookApp.recipe.home.createLabel">Create new Recipe</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {recipeList && recipeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="cookbookApp.recipe.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="cookbookApp.recipe.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="cookbookApp.recipe.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {recipeList.map((recipe, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${recipe.id}`} color="link" size="sm">
                      {recipe.id}
                    </Button>
                  </td>
                  <td>{recipe.name}</td>
                  <td>{recipe.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${recipe.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${recipe.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${recipe.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="cookbookApp.recipe.home.notFound">No Recipes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Recipe;
