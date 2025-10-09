#!/bin/bash
# Script für echtes Hot Reload - kopiert Dateien automatisch

# Überwache src/main/resources für Änderungen
fswatch -o src/main/resources/ | while read f; do
    echo "Änderung erkannt - kopiere Ressourcen..."
    
    # Kopiere Templates
    cp -r src/main/resources/templates/* build/resources/main/templates/ 2>/dev/null || true
    
    # Kopiere Static Files
    cp -r src/main/resources/static/* build/resources/main/static/ 2>/dev/null || true
    
    echo "Ressourcen aktualisiert - Browser refreshen!"
done