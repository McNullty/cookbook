import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, Table } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './recipes.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getIngredients} from "app/entities/ingredient/ingredient.reducer";
import {getEntities as getUnits} from "app/entities/unit/unit.reducer";

export const RecipeWithDetailsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const recipeEntity = useAppSelector(state => state.recipeWithDetail.entity);
  const loading = useAppSelector(state => state.recipeWithDetail.loading);
  const updating = useAppSelector(state => state.recipeWithDetail.updating);
  const updateSuccess = useAppSelector(state => state.recipeWithDetail.updateSuccess);

  const ingredients = useAppSelector(state => state.ingredient.entities);
  const units = useAppSelector(state => state.unit.entities);

  const handleClose = () => {
    props.history.push('/recipes');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getIngredients({}));
    dispatch(getUnits({}));

  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...recipeEntity,
      ...values,
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
          ...recipeEntity,
      };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cookbookApp.recipe.home.createOrEditLabel" data-cy="RecipeCreateUpdateHeading">
            <Translate contentKey="cookbookApp.recipe.home.createOrEditLabel">Create or edit a Recipe</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField
                label={translate('cookbookApp.recipe.name')}
                id="recipe-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />


              {/* Ingidients */}

              <div className="table-responsive">
                {recipeEntity.recipeItems && recipeEntity.recipeItems.length > 0 ? (
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
                    {recipeEntity.recipeItems.map((recipeItem, i) => (
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
                      <Translate contentKey="cookbookApp.recipe.home.notFound">No ingredients found</Translate>
                    </div>
                  )
                )}
              </div>

              {/* Ingidients */}

              <ValidatedField
                label={translate('cookbookApp.recipe.description')}
                id="recipe-description"
                name="description"
                data-cy="description"
                type="text"
              />

              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/recipes" replace color="info">
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

export default RecipeWithDetailsUpdate;
