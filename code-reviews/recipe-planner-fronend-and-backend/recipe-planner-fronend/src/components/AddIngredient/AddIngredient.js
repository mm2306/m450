import React from 'react';
import './AddIngredient.css';
import {Button, Col, Form, Row} from "react-bootstrap";

const UNIT_OPTIONS = ['PIECE', 'GRAMM', 'KILOGRAMM', 'LITRE', 'DECILITRE'];

const AddIngredient = ({ingredient, updateIngredient, removeIngredient}) => {

    const handleChange = (field, value) => {
        updateIngredient({...ingredient, [field]: value});
    };

    return (
        <Row>
            <Col>
                <Form.Group className="mb-1">
                    <Form.Control
                        value={ingredient.name}
                        onChange={e => handleChange('name', e.target.value)}
                        placeholder="Name"
                        required
                    />
                </Form.Group>
            </Col>
            <Col>
                <Form.Group className="mb-1">
                    <Form.Select
                        value={ingredient.unit}
                        onChange={e => handleChange('unit', e.target.value)}
                    >
                        {UNIT_OPTIONS.map(unit => <option key={unit}>{unit}</option>)}
                    </Form.Select>
                </Form.Group>
            </Col>
            <Col>
                <Form.Group className="mb-1">
                    <Form.Control
                        type="number"
                        min="0"
                        value={ingredient.amount}
                        onChange={e => handleChange('amount', e.target.value === '' ? '' : parseInt(e.target.value))}
                        placeholder="Quantity"
                        required
                    />
                </Form.Group>
            </Col>
            <Col xs={1}>
                <Button
                    onClick={() => removeIngredient(ingredient)}
                    variant='outline-dark'
                    className="mb-1"
                >x</Button>
            </Col>
        </Row>
    );
};

export default AddIngredient;