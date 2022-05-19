import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, Table, Form, Input, FormGroup, Label } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './recipes.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getIngredients} from "app/entities/ingredient/ingredient.reducer";
import {getEntities as getUnits} from "app/entities/unit/unit.reducer";
import { v4 as uuidv4 } from 'uuid';
import {IRecipeWithDetails} from "app/shared/model/recipe-with-details.model";
import {Controller, useFieldArray, useForm } from 'react-hook-form';

export const RecipeWithDetailsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const {register, handleSubmit, formState: { errors }, control, setValue } = useForm<IRecipeWithDetails>();

  const {fields, append, remove} = useFieldArray({control, name: "recipeItems"});

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const ingredients = useAppSelector(state => state.ingredient.entities);
  const ingredientsLoading = useAppSelector(state => state.ingredient.loading);
  const units = useAppSelector(state => state.unit.entities);
  const unitsLoading = useAppSelector(state => state.unit.loading);

  const recipeEntity = useAppSelector(state => state.recipeWithDetail.entity);
  const loading = useAppSelector(state => state.recipeWithDetail.loading);
  const updating = useAppSelector(state => state.recipeWithDetail.updating);
  const updateSuccess = useAppSelector(state => state.recipeWithDetail.updateSuccess);

  const handleClose = () => {
    props.history.push('/recipes');
  };

  useEffect(() => {
    setValue("name", recipeEntity.name);
    setValue("description", recipeEntity.description);
    setValue("recipeItems", recipeEntity.recipeItems);
  }, [recipeEntity]);

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
      id: props.match.params.id
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

  const onSubmit = (data: IRecipeWithDetails) => {
    console.error("data", data);
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
          {loading || ingredientsLoading || unitsLoading ? (
            <p>Loading...</p>
          ) : (
            <Form onSubmit={handleSubmit(onSubmit)}>
              <FormGroup row>
                <Label for="name">Name</Label>
                <Controller
                  defaultValue={recipeEntity.description}
                  name="name"
                  control={control}
                  render = {({ field }) =>(
                  <Input
                    type="text"
                    {...field}
                  />
                )}/>
              </FormGroup>
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
                {fields.map((field, index) => (
                  <tr key={field.id}>
                    <td scope="row">
                      <Controller
                        defaultValue = {field.ingredient}
                        name={`recipeItems.${index}.ingredient`}
                        control={control}
                        render = {({ field: renderField }) =>(
                          <Input
                            type="select"
                            {...renderField}
                          >
                            <option value="" key="0" />
                            {ingredients
                              ? ingredients.map(otherEntity => (
                                <option value={otherEntity.name} key={otherEntity.id} >
                                  {otherEntity.name}
                                </option>
                              ))
                              : null}
                          </Input>
                        )}/>
                    </td>
                    <td scope="row">
                      <Controller
                        defaultValue = {field.quantity}
                        name={`recipeItems.${index}.quantity`}
                        control={control}
                        render = {({ field: renderField }) =>(
                          <Input
                            type="number"
                            {...renderField}
                          />
                        )}/>
                    </td>
                    <td scope="row">
                      <Controller
                        defaultValue = {field.unit}
                        name={`recipeItems.${index}.unit`}
                        control={control}
                        render = {({ field: renderField }) =>(
                          <Input
                            type="select"
                            {...renderField}
                          >
                            <option value="" key="0" />
                            {units
                              ? units.map(otherEntity => (
                                <option value={otherEntity.name} key={otherEntity.id}>
                                  {otherEntity.name}
                                </option>
                              ))
                              : null}
                          </Input>
                        )}/>
                    </td>
                    <td>
                      <Button
                        color="primary"
                        outline
                        type="button"
                        onClick = {() => remove(index)}>Delete</Button>
                    </td>
                  </tr>
                ))}
                </tbody>
              </Table>

              <Button
                color="primary"
                outline
                type="button"
                onClick = {() => append({})}>Add</Button>
              <FormGroup row>
                <Label for="description">Description</Label>
                <Controller
                  defaultValue={recipeEntity.description}
                  name="description"
                  control={control}
                  render = {({ field }) =>(
                    <Input
                      type="text"
                      {...field}
                    />
                )}/>
              </FormGroup>
              <Button color="primary" type="submit">Save</Button>
            </Form>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RecipeWithDetailsUpdate;
