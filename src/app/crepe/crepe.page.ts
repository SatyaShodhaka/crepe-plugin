import { Component, OnInit } from '@angular/core';
import { Crepe } from 'crepe-plugin';

@Component({
    selector: 'app-crepe',
    templateUrl: './crepe.page.html',
    styleUrls: ['./crepe.page.scss'],
})
export class CrepePage implements OnInit {
    isModalOpen = false;
    modalStep = 'main';

    installedApps: any[] = [];
    selectedApp: string = '';
    startDate: string = '';
    endDate: string = '';
    collectorData: any = null;
    matchingResults: any = null;

    constructor() { }

    ngOnInit() {
        this.startAccessibility();
        this.initializeGraphQuery();
    }

    async startAccessibility() {
        try {
            console.log('Starting Accessibility Service...');
            await Crepe.startAccessibilityService();
            console.log('Service started successfully');
        } catch (error) {
            console.error('Failed to start service:', error);
        }
    }

    async stopAccessibility() {
        try {
            console.log('Stopping Accessibility Service...');
            await Crepe.stopAccessibilityService();
            console.log('Service stopped');
        } catch (error) {
            console.error('Failed to stop service:', error);
        }
    }

    async initializeGraphQuery() {
        try {
            await Crepe.initializeGraphQuery();
            console.log('Graph query system initialized');
        } catch (error) {
            console.error('Failed to initialize graph query:', error);
        }
    }

    async updateSnapshot() {
        try {
            await Crepe.updateSnapshot();
            console.log('Snapshot updated');
        } catch (error) {
            console.error('Failed to update snapshot:', error);
        }
    }

    async matchCollectorData() {
        try {
            // Sample collector data - replace with your actual data
            const sampleCollectorData = {
                collectors: {
                    "collector1": {
                        "appName": "Sample App",
                        "appPackage": "com.example.app",
                        "collectorId": "collector1",
                        "collectorStatus": "active"
                    }
                },
                dataFields: {
                    "field1": {
                        "collectorId": "collector1",
                        "datafieldId": "field1",
                        "name": "Username Field",
                        "graphQuery": "node[text='Username']"
                    },
                    "field2": {
                        "collectorId": "collector1",
                        "datafieldId": "field2",
                        "name": "Password Field",
                        "graphQuery": "node[text='Password']"
                    }
                },
                data: {
                    "data1": {
                        "dataContent": "sample data",
                        "datafieldId": "field1",
                        "userId": "user1"
                    }
                }
            };

            this.collectorData = sampleCollectorData;
            const result = await Crepe.matchCollectorData({
                collectorData: JSON.stringify(sampleCollectorData)
            });

            this.matchingResults = result.matches;
            console.log('Matching results:', this.matchingResults);
        } catch (error) {
            console.error('Failed to match collector data:', error);
        }
    }

    openModal() {
        this.isModalOpen = true;
    }

    closeModal() {
        this.isModalOpen = false;
        this.modalStep = 'main';
    }

    createNew() {
        this.modalStep = 'createNew';
    }

    addExisting() {
        // Implement add existing collector logic
        console.log('Add existing collector');
    }

    goBack() {
        if (this.modalStep === 'createNew') {
            this.modalStep = 'main';
        }
    }

    async submitForm() {
        // Implement form submission logic
        console.log('Form submitted:', {
            startDate: this.startDate,
            endDate: this.endDate,
            selectedApp: this.selectedApp
        });

        // After form submission, initialize matching
        await this.initializeGraphQuery();
        await this.updateSnapshot();
        await this.matchCollectorData();

        this.closeModal();
    }
} 