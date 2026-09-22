import React, {useRef, useState} from 'react';
import './AddRecipe.css';
import {useForm} from "react-hook-form";
import {Alert, Button, Col, Form, Row} from 'react-bootstrap';
import axios from "axios";
import AddIngredient from "../AddIngredient/AddIngredient";

const baseURL = "http://localhost:8080/api/recipes";

function AddRecipe() {
    const [ingredientList, setIngredientList] = useState([]);
    const [submitState, setSubmitState] = useState({submitting: false, error: '', success: false});
    const {register, handleSubmit, formState: {errors}, reset} = useForm();
    const nextListId = useRef(1);

    const addIngredient = () => {
        setIngredientList([...ingredientList, {listId: nextListId.current++, name: '', unit: 'PIECE', amount: ''}]);
    };

    const updateIngredient = (updatedIngredient) => {
        setIngredientList(ingredientList.map(ingredient =>
            ingredient.listId === updatedIngredient.listId ? updatedIngredient : ingredient
        ));
    };

    const removeIngredient = (ingredient) => {
        setIngredientList(ingredientList.filter(i => i.listId !== ingredient.listId));
    };

    const onSubmit = async (formValues) => {
        const recipe = {
            name: formValues.name,
            description: formValues.description,
            imageUrl: formValues.imageUrl,
            ingredients: ingredientList.map(({listId, ...ingredient}) => ({
                ...ingredient,
                amount: ingredient.amount === '' ? 0 : ingredient.amount,
            })),
        };
        setSubmitState({submitting: true, error: '', success: false});
        try {
            await axios.post(baseURL, recipe);
            setSubmitState({submitting: false, success: true});
            setIngredientList([]);
            reset();
        } catch (error) {
            setSubmitState({submitting: false, error: error.message});
        }
    };

    return (
        <div className="bg">
            <div className="m-3">
                <h1 className="h3 bg-dark text-bg-primary mt-2">Add Recipe</h1>
                <Form onSubmit={handleSubmit(onSubmit)}>
                    <Form.Group className="mb-1" controlId="formBasicName">
                        <Form.Label>Recipe Name:</Form.Label>
                        <Form.Control
                            placeholder="Name"
                            {...register('name', {required: 'Recipe name is required'})}
                            isInvalid={!!errors.name}
                        />
                        <Form.Control.Feedback type="invalid">{errors.name?.message}</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group className="mb-1" controlId="formBasicDescription">
                        <Form.Label>Description:</Form.Label>
                        <Form.Control
                            placeholder="Description"
                            {...register('description')}
                        />
                    </Form.Group>
                    <Form.Group className="mb-1 mb-5" controlId="formBasicImageUrl">
                        <Form.Label>Image URL:</Form.Label>
                        <Form.Control
                            placeholder="URL"
                            {...register('imageUrl')}
                        />
                    </Form.Group>

                    <Row>
                        <Col>Ingredient</Col>
                        <Col>Unit</Col>
                        <Col>Quantity</Col>
                        <Col xs={1}></Col>
                    </Row>
                    <hr/>
                    <Row>
                        <br></br>
                    </Row>
                    {ingredientList.map(ingredient => (
                        <AddIngredient
                            key={ingredient.listId}
                            ingredient={ingredient}
                            updateIngredient={updateIngredient}
                            removeIngredient={removeIngredient}
                        />
                    ))}
                    <Row>
                        <br></br>
                        <Button
                            variant='warning'
                            onClick={addIngredient}
                            className="mt-1"
                        >Add Ingredient</Button>
                    </Row>

                    {submitState.error && <Alert variant="danger" className="mt-3">{submitState.error}</Alert>}
                    {submitState.success && <Alert variant="success" className="mt-3">Recipe created successfully.</Alert>}

                    <Button
                        variant="primary"
                        type="submit"
                        disabled={submitState.submitting}
                        className="mb-5"
                    >
                        {submitState.submitting ? 'Submitting...' : 'Submit'}
                    </Button>
                </Form>
            </div>
        </div>
    );
}

export default AddRecipe;
