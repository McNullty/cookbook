import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IngredientForRecipe from './ingredient-for-recipe';
import IngredientForRecipeDetail from './ingredient-for-recipe-detail';
import IngredientForRecipeUpdate from './ingredient-for-recipe-update';
import IngredientForRecipeDeleteDialog from './ingredient-for-recipe-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IngredientForRecipeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IngredientForRecipeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IngredientForRecipeDetail} />
      <ErrorBoundaryRoute path={match.url} component={IngredientForRecipe} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={IngredientForRecipeDeleteDialog} />
  </>
);

export default Routes;
