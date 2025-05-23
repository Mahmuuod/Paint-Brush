# Java Paint Brush Application

## Project Overview

This Java project implements a versatile **Paint Brush application** demonstrating core Object-Oriented Programming (OOP) principles. It allows users to draw on a digital canvas with multiple brush types, manage their work through undo operations, and save/load images for later editing.

Designed with modularity and scalability in mind, it serves both as a functional drawing tool and a strong example of clean OOP design.

---

## Features

- Freehand drawing with mouse input.
- Support for multiple brush tools:  
  - Freehand brush  
  - Rectangles  
  - Ovals  
  - Straight lines
- Configurable brush sizes and colors.
- Undo functionality to revert last drawing actions.
- Ability to save the current drawing as an image file (e.g., PNG).
- Ability to load saved images back into the app for further editing.
- Real-time rendering of strokes and shapes on the canvas.
- Structured, maintainable OOP design facilitating easy feature extension.

---

## Architecture and Design

### Classes and Responsibilities

| Class Name           | Responsibility                                                                                      |
|----------------------|---------------------------------------------------------------------------------------------------|
| **`Brush`**          | Abstract base class encapsulating brush properties such as size, color, and basic behaviors.      |
| **`PaintBrush`**     | Concrete subclass implementing freehand drawing logic and stroke management.                       |
| **`ShapeBrush`**     | Abstract subclass for shape tools (rectangles, ovals, lines) with shared shape logic.             |
| **`RectangleBrush`** | Concrete implementation for drawing rectangles.                                                   |
| **`OvalBrush`**      | Concrete implementation for drawing ovals.                                                        |
| **`LineBrush`**      | Concrete implementation for drawing straight lines.                                               |
| **`Stroke`**         | Represents a drawing action — either freehand strokes or shapes — with properties for color, thickness, and geometry. |
| **`Canvas`**         | The drawing surface that manages rendering and stores a history of strokes for undo support.      |
| **`ImageHandler`**   | Handles saving the canvas to image files and loading images back for editing.                      |
| **`ColorPicker`**    | UI component for selecting brush colors.                                                          |
| **`BrushSizeSelector`** | UI component for selecting brush thickness.                                                     |
| **`Main`**           | Application entry point setting up UI, event listeners, and managing application lifecycle.       |

---

## Object-Oriented Principles Demonstrated

- **Encapsulation:** Brush and stroke properties are encapsulated to hide complexity.
- **Inheritance:** Brush hierarchy supports easy addition of new drawing tools.
- **Polymorphism:** Shapes and freehand brushes implement common interfaces allowing uniform handling.
- **Abstraction:** Abstracts drawing details away from UI interaction.
- **Composition:** Canvas maintains a collection of strokes for rendering and undo management.

---

## Usage Instructions

1. **Compile the Project**

   ```bash
   javac *.java
