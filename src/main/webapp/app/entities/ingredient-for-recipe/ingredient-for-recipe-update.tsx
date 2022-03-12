import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IIngredient } from 'app/shared/model/ingredient.model';
import { getEntities as getIngredients } from 'app/entities/ingredient/ingredient.reducer';
import { IUnit } from 'app/shared/model/unit.model';
import { getEntities as getUnits } from 'app/entities/unit/unit.reducer';
import { IRecipe } from 'app/shared/model/recipe.model';
import { getEntities as getRecipes } from 'app/entities/recipe/recipe.reducer';
import { getEntity, updateEntity, createEntity, reset } from './ingredient-for-recipe.reducer';
import { IIngredientForRecipe } from 'app/shared/model/ingredient-for-recipe.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const IngredientForRecipeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const ingredients = useAppSelector(state => state.ingredient.entities);
  const units = useAppSelector(state => state.unit.entities);
  const recipes = useAppSelector(state => state.recipe.entities);
  const ingredientForRecipeEntity = useAppSelector(state => state.ingredientForRecipe.entity);
  const loading = useAppSelector(state => state.ingredientForRecipe.loading);
  const updating = useAppSelector(state => state.ingredientForRecipe.updating);
  const updateSuccess = useAppSelector(state => state.ingredientForRecipe.updateSuccess);
  const handleClose = () => {
    props.history.push('/ingredient-for-recipe');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getIngredients({}));
    dispatch(getUnits({}));
    dispatch(getRecipes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...ingredientForRecipeEntity,
      ...values,
      ingredient: ingredients.find(it => it.id.toString() === values.ingredient.toString()),
      unit: units.find(it => it.id.toString() === values.unit.toString()),
      recipe: recipes.find(it => it.id.toString() === values.recipe.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...ingredientForRecipeEntity,
          ingredient: ingredientForRecipeEntity?.ingredient?.id,
          unit: ingredientForRecipeEntity?.unit?.id,
          recipe: ingredientForRecipeEntity?.recipe?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cookbookApp.ingredientForRecipe.home.createOrEditLabel" data-cy="IngredientForRecipeCreateUpdateHeading">
            <Translate contentKey="cookbookApp.ingredientForRecipe.home.createOrEditLabel">Create or edit a IngredientForRecipe</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="ingredient-for-recipe-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cookbookApp.ingredientForRecipe.quantity')}
                id="ingredient-for-recipe-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="ingredient-for-recipe-ingredient"
                name="ingredient"
                data-cy="ingredient"
                label={translate('cookbookApp.ingredientForRecipe.ingredient')}
                type="select"
                required
              >
                <option value="" key="0" />
                {ingredients
                  ? ingredients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="ingredient-for-recipe-unit"
                name="unit"
                data-cy="unit"
                label={translate('cookbookApp.ingredientForRecipe.unit')}
                type="select"
                required
              >
                <option value="" key="0" />
                {units
                  ? units.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="ingredient-for-recipe-recipe"
                name="recipe"
                data-cy="recipe"
                label={translate('cookbookApp.ingredientForRecipe.recipe')}
                type="select"
                required
              >
                <option value="" key="0" />
                {recipes
                  ? recipes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ingredient-for-recipe" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default IngredientForRecipeUpdate;
